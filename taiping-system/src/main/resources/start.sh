#!/usr/bin/env bash

#  一键启动脚本
#  本脚本的前提是 所有的基础环境已经安装完毕
#############################################
#
#   服务名称    版本      端口
#   java      1.8       8082(后台服务 通过 nginx 代理)
#   mysql     5.7      3306
#   nginx    1.14.2    4200 (前端服务运行环境 )
#   vsftp              21   (服务上传下载端口)
#
#############################################

currentPath=`pwd`
myPath="/taiping"


# 默认配置文件
Host=`/sbin/ifconfig -a|grep inet|grep -v 127.0.0.1|grep -v inet6|sed -n "1,1p"|awk '{print $2}'|tr -d "addr:"`
mailAddr="tpma2001.wistronits.com"
userName="yujieli@wistronits.com"
passWord="Heimou123"

taipingStatus=`ps -ef|grep taiping|grep -v grep|cut -c 9-15`
function taiping_check_status() {
   if [[ ${taipingStatus} != null ]]; then
      kill -9 ${taipingStatus}
   fi
}

nginxStatus=`ps -ef|grep nginx|grep -v grep|cut -c 9-15`
function nginx_check_status() {
     if [[ ${nginxStatus} != null ]]; then
      kill -9 ${nginxStatus}
   fi
}


function taiping_check() {
 if [[ ! -d ${myPath} ]]; then
       mkdir ${myPath}
 fi
 }


function start_mysql() {
echo "启动mysql "
systemctl stop mysqld.service
systemctl start mysqld.service
echo "mysql 启动完毕>>>>>>"
}

function start_ftp() {
    echo "启动 ftp>>>>>>>>>"
    systemctl  stop vsftpd.service
    systemctl start vsftpd.service

    echo "是否是第一次配置启动,确认请输入 y,不是请按 Enter 键跳过 "
    read inputFirst
    if [[ ${inputFirst} -eq "y" ]]; then
      firewall-cmd --permanent --zone=public --add-service=ftp
      firewall-cmd --reloa
    fi
    echo "启动 ftp 完毕 >>>>>>>"
}



function start_nginx() {
 echo "nginx 启动"
 rm -rf /usr/local/nginx/dist
 rm -rf /usr/local/nginx/web.tar.gz
 cp ${currentPath}/jar/web.tar.gz /usr/local/nginx
 cd /usr/local/nginx
 tar -zxvf web.tar.gz
 nginx_check_status
# ps -ef|grep nginx|grep -v grep|cut -c 9-15|xargs kill -9
 /usr/local/nginx/sbin/nginx
 echo "nginx 启动完毕>>>>>>>"
}

function start_system() {
   echo "启动后台服务"
taiping_check_status
mysqlUrl="jdbc:mysql://$Host:3306/taiping?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=UTC"

cd /taiping/

#ps -ef | grep taiping-system-1.0-SNAPSHOT.jar| grep -v grep|awk '{print $2}'| xargs kill -9
nohup java -jar taiping-system-1.0-SNAPSHOT.jar --spring.profiles.active=sit --spring.datasource.url=${mysqlUrl} --ftp.host=${Host} --spring.mail.host=${mailAddr} --spring.mail.username=${userName} --spring.mail.password=${passWord}  > taiping.log 2>&1 &

echo "后台服务启动完毕>>>>>>>>"

}


#step-1
# 启动 mysql
echo "是否需要启动mysql? 确认请输入 y,不需要启动请按 Enter 键跳过"
read inputMysql
if [[ ${inputMysql} == "y" ]]; then
    start_mysql
fi

#step-2
# 启动 vftp
echo "是否需要启动 ftp? 确认请输入 y,不需要启动请按 Enter 键跳过"
read inputFtp
if [[ ${inputFtp} == "y" ]]; then
    start_ftp
fi

#step-3
#启动 nginx
echo "是否需要启动 nginx ? 确认请输入 y,不需要启动请按 Enter 键跳过"
read inputNginx
if [[ ${inputNginx} == "y" ]]; then
     echo "请确保前台包 web.tar.gz 在脚本目录下>>>>>"

    start_nginx
fi

echo "是否需要启动后台服务 ? 确认请输入 y,不需要启动请按 Enter 键跳过"
read inputSystem
if [[ ${inputSystem} == "y" ]]; then
  echo "请确保后台包 taiping-system-1.0-SNAPSHOT.jar 在脚本目录下>>>>>"
    taiping_check
    rm -rf /taiping/taiping-system-1.0-SNAPSHOT.jar
    cp ${currentPath}/jar/taiping-system-1.0-SNAPSHOT.jar /taiping/
    start_system
fi

echo "开始访问服务>>>>>>>"

echo "访问地址: http://$Host:4200"
echo "默认用户名:admin"
echo "默认密码 ： admin123 "

echo "mysql 访问地址为: $Host:3306"
echo "mysql 用户名为: root"
echo "mysql 密码为: wistronits@123 "

























