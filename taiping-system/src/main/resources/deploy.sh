#!/usr/bin/env bash
###################################
#  一键 部署脚本
# author : liyj
# date:  2019/11/15
# 本脚本 将一键安装
#   java      1.8
#   mysql     5.7      3306
#   nginx    1.14.2    4200 (前端服务运行环境 )
#   vsftp              21   (服务上传下载端口)
#  安装需要确保有 yum 和 全新环境
#  未安装 以上服务 不适用本版本
#####################################

currentPath=`pwd`
fireWalldFlag=`ps -ef|grep firewalld|grep -v grep|cut -c 9-15`

function firewalld_check() {
   if [[ ${fireWalldFlag} -eq null ]]; then
       systemctl start firewalld
   fi
}


#java 源码 安装
function yum_java8_install() {
    echo "开始安装java 8 yum 源处理"
    yum -y install java-1.8.0-openjdk-devel.x86_64
    echo "JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.181-3.b13.el7_5.x86_64" >> /etc/profile
    echo "JRE_HOME=$JAVA_HOME/jre" >> /etc/profile
    echo "PATH=$PATH:$JAVA_HOME/bin" >> /etc/profile
    echo "CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar" >>/etc/profile
    echo "export JAVA_HOME" >>/etc/profile
    echo "export PATH" >>/etc/profile
    echo "export CLASSPATH" >>/etc/profile
    source /etc/profile
   echo "java8 环境安装完毕,现在开始检测是否ok"
  version=`java -version`
  echo "java 版本"$ version
}

# nginx 源码安装
function yum_nginx_install() {
        echo "开始安装 nginx1.14.2 >>>>>>"
         yum -y install gcc pcre-devel openssl openssl-devel
         wget https://nginx.org/download/nginx-1.14.0.tar.gz
         tar -xzvf nginx-1.14.0.tar.gz
         mv nginx-1.14.0 /usr/local/nginx
         rm -rf nginx-1.14.0.tar.gz
#         echo "将准备好的nginx.conf 替换到 /usr/local/nginx/conf/nginx.conf"
         mv /usr/local/nginx/conf/nginx.conf /usr/local/nginx/conf/nginx.cong.bak
         mv ${currentPath}/conf/nginx.conf /usr/local/nginx/conf/nginx.conf
         # 进入nginx 目录配置   检测配置./configure
         cd /usr/local/nginx/
         # 新建文件夹 logs
         mkdir logs
         ./configure
         echo "配置文件检测是否出现 ./configure: error提示,如果没有 请按Entry 进行下一步编译,\
            否则请按 n 退出当前安装，解决nginx_check 错误后 重新安装"
         read inputCheckNginx
         if [[ ${inputCheckNginx} == 'n' ]]; then
             # 删除nginx 源码
             echo "结束安装！"
          else
             echo "开始编译nginx"
             make
             make install
             echo "export NGINX_HOME=/usr/local/nginx" >>/etc/profile
             echo "export PATH=$PATH:$NGINX_HOME/sbin" >>/etc/profile
             source /etc/profile
             firewalld_check
             firewall-cmd --permanent --zone=public --add-port=4200/tcp;
             firewall-cmd --reload
             /usr/local/nginx/sbin/nginx
             echo "nginx 安装完毕>>>>>>>>>>"
         fi


}

# vstp yum 安装
function yum_vsftp_install() {
    echo "开始安装 vsftp 服务>>>>>>>"
    yum install -y ftp
    yum install -y vsftpd

    # 修改配置文件 重新写入所有的配置文件
    rm -rf /etc/vsftpd/vsftpd.conf
    touch /etc/vsftpd/vsftpd.conf

    echo "# Example config file /etc/vsftpd/vsftpd.conf" >>/etc/vsftpd/vsftpd.conf
    echo "anonymous_enable=YES" >>/etc/vsftpd/vsftpd.conf
    echo "local_enable=YES" >>/etc/vsftpd/vsftpd.conf
    echo "write_enable=YES" >>/etc/vsftpd/vsftpd.conf
    echo "local_umask=022" >>/etc/vsftpd/vsftpd.conf
    echo "anon_upload_enable=YES" >>/etc/vsftpd/vsftpd.conf
    echo "anon_mkdir_write_enable=YES" >>/etc/vsftpd/vsftpd.conf
    echo "dirmessage_enable=YES" >>/etc/vsftpd/vsftpd.conf
    echo "xferlog_enable=YES" >>/etc/vsftpd/vsftpd.conf
    echo "connect_from_port_20=YES" >>/etc/vsftpd/vsftpd.conf
    echo "anon_other_write_enable=YES" >>/etc/vsftpd/vsftpd.conf
    echo "xferlog_std_format=YES" >>/etc/vsftpd/vsftpd.conf
    echo "listen=NO" >>/etc/vsftpd/vsftpd.conf
    echo "listen_ipv6=YES" >>/etc/vsftpd/vsftpd.conf
    echo "anon_umask=077" >>/etc/vsftpd/vsftpd.conf
    echo "userlist_enable=YES" >>/etc/vsftpd/vsftpd.conf
    echo "tcp_wrappers=YES" >>/etc/vsftpd/vsftpd.conf
    echo "anon_root=/var/ftp" >>/etc/vsftpd/vsftpd.conf
    echo "anon_world_readable_only=NO" >>/etc/vsftpd/vsftpd.conf

   systemctl start vsftpd.service
    # 添加开机启动
    systemctl enable vsftpd.service
    # 添加防火墙访问规则
    ps -ef|grep vsftp|grep -v grep|cut -c 9-15|xargs kill -9
    systemctl start vsftpd
    firewalld_check
    firewall-cmd --permanent --zone=public --add-service=ftp
    firewall-cmd --reload
    # 后面需要修改 ftpusers 文件中的 root 权限

    echo "结束安装 vsftp 服务>>>>>>>"
}


# mysql 5.7 yum 安装
function yum_mysql_install() {
    echo "开始安装 mysql 服务>>>>>>>"
     mkdir /taipingtest
    cp ${currentPath}/install_db/mysql_install.sql /taipingtest/

 #  下载并安装Mysql 官方的yum Repository
   wget -i -c http://dev.mysql.com/get/mysql57-community-release-el7-10.noarch.rpm
   # yum 安装
  yum install -y mysql57-community-release-el7-10.noarch.rpm
   # 安装mysql 服务器
   yum install -y mysql-community-server

  # 修改mysql配置 设置 only_full_group_by 解决 查询结果不在 group by
  echo "sql_mode =STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION" >> /etc/my.ini
  # 启动mysql
  systemctl start mysqld.service
  inputPassword="wistronits@123"

  oldpwd=`grep "root@localhost" /var/log/mysqld.log|awk '{print $11}'`
    echo "开始配置mysql "
   # 修改mysql 密码
   echo "你的旧密码是:${oldpwd}"
   echo "将 下面的sql 复制到控制台>>>>>>>>>"
   echo " set global validate_password_policy=0;"
   echo " set global validate_password_mixed_case_count=0;"
   echo " set global validate_password_number_count=3;"
   echo " set global validate_password_special_char_count=0;"
   echo " set global validate_password_length=3;"
   echo "ALTER USER 'root'@'localhost' IDENTIFIED BY '${inputPassword}';"
   echo "use mysql;"
   echo "update user set host = '%' where user = 'root';"
   echo "flush privileges;"
   echo "source /taipingtest/mysql_install.sql"

   echo "全部输入完成后 输入exit 退出mysql 客户端"
# 登录mysql
   mysql -uroot -p

   echo "mysql数据库重启中......"
   firewalld_check
   firewall-cmd --permanent --zone=public --add-port=4200/tcp;
   firewall-cmd --reload
   systemctl restart mysqld
   systemctl status mysqld
   rm -rf /taipingtest
   echo "结束安装 mysql 服务>>>>>>>"
}

function check_date() {
     yum install -y ntp
     systemctl enable ntpd
     rm -rf /etc/sysconfig/ntpd
     touch /etc/sysconfig/ntpd
     echo " OPTIONS=`"-g -x"` " >> /etc/sysconfig/ntpd
     systemctl start ntpd.service
     ln -sf /usr/share/zoneinfo/Asia/Shanghai/etc/localtime
     echo "服务器系统时间:`date`"
}


#step-1
# 配置 mysql
echo "是否需要安装 mysql? 确认请输入 y,不需要启动请按 Enter 键跳过"
read inputMysql
if [[ ${inputMysql} == "y" ]]; then
    echo "请确保数据库初始脚本 mysql_install.sql 在同级目录"
    yum_mysql_install
fi

#step-2
# 启动 vftp
echo "是否需要安装 ftp? 确认请输入 y,不需要启动请按 Enter 键跳过"
read inputFtp
if [[ ${inputFtp} == "y" ]]; then
    yum_vsftp_install
fi

#step-3
#启动 nginx
echo "是否需要安装 nginx ? 确认请输入 y,不需要启动请按 Enter 键跳过"
read inputNginx
if [[ ${inputNginx} == "y" ]]; then
    echo "请准备好配置文件 nginx.conf 在脚本同级目录>>>>>>>>"
    yum_nginx_install
fi


echo "是否需要安装jdk 1.8 ? 确认请输入 y,不需要启动请按 Enter 键跳过"
read inputSystem
if [[ ${inputSystem} == "y" ]]; then
    yum_java8_install
fi

echo "开始时间校对 默认为东八区时间? 确认请输入 y,不需要启动请按 Enter 键跳过"
read inputTime
if [[ ${inputTime} == "y" ]]; then
    check_date
fi


echo "所有的服务应该安装完毕,你可以使用start.sh 一键启动服务了"





