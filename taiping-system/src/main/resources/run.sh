#!/bin/sh
#mysqlURL 地址
Host=`/sbin/ifconfig -a|grep inet|grep -v 127.0.0.1|grep -v inet6|sed -n "1,1p"|awk '{print $2}'|tr -d "addr:"`
mysqlPort=3306
mysqlUrl="jdbc:mysql://$Host:$mysqlPort/taiping?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=UTC"


# 系统管理员邮箱配置
mailAddr="tpma2001.wistronits.com"
userName="yujieli@wistronits.com"
passWord="Heimou123"


cd /taiping/
ps -ef | grep taiping-system-1.0-SNAPSHOT.jar| grep -v grep|awk '{print $2}'| xargs kill -9
nohup java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=10000   -jar taiping-system-1.0-SNAPSHOT.jar --spring.profiles.active=sit --spring.datasource.url=${mysqlUrl} --ftp.host=${Host} --spring.mail.host=${mailAddr} --spring.mail.username=${userName} --spring.mail.password=${passWord} > taiping.log 2>&1 &


echo "taiping-system-project Running......"
tail -f taiping.log