#!/usr/bin/expect
set password "Aliyun1378"
spawn scp -r /Users/lane/IdeaProjects/lms/lms-admin/dist/ root@120.79.91.131:/data/wwwroot/default/
expect "password:"
send "${password}\n"
set timeout 300
send "exit\n"
expect eof
