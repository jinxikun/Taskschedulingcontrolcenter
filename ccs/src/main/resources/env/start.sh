#!/bin/sh
tpid=`cat tpid|awk '{print $1}'`
tpid=`ps -aef|grep $tpid|awk '{print $2}'|grep $tpid`
if [ ${tpid} ]
then
  echo App is already running!!!
else
  rm -f tpid
  nohup java -jar ccs-0.0.1-SNAPSHOT.jar  --spring.config.location=./cfg/application.yml --logging.config=./cfg/logback.xml > /dev/null 2>&1 &
  echo 'ccs server is started.'
  echo $! > tpid
fi
