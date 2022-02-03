#!/bin/bsh
### Begin init info
# Provides: Yester
### End init info

SERVICE_NAME=Yester
PATH_TO_JAR=/home/curi/yester/target/scala-2.12/yester-assembly-0.2.5.jar
PID_PATH_NAME=/tmp/yester-pid

case $1 in
    start)
        echo "starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            nohup scala $PATH_TO_JAR > /dev/null 2>&1 &
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running"
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stopping ..."
            kill -9 $PID
            echo "$SERVICE_NAME stopped ..."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    restart)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME)
            echo "$SERVICE_NAME stopping ...";
            kill -9 $PID
            echo "$SERVICE_NAME stopped ...";
            rm $PID_PATH_NAME
            echo "$SERVICE_NAME starting ..."
            nohup scala $PATH_TO_JAR > /dev/null 2>&1 &
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac
