#!/bin/bash
echo "start to run test.sh script"

FILENAME="$1"
TIMEFILE="/tmp/loopfile.out" > $TIMEFILE
SCRIPT=$(basename $0)

function usage(){
    echo -e "\nUSAGE: ${SCRIPT} file \n"
    exit 1
}

function while_read_bottom(){
    while read LINE
    do
        echo -n ${LINE}
    done < $FILENAME
}

function while_read_line(){
    cat $FILENAME | while read LINE
    do
        echo $LINE
    done
}

function while_read_fd(){
    exec 3<&0
    exec 0<$FILENAME
    while read LINE
    do
        echo $LINE
    done
    exec 0<&3
}

function for_in_file(){
    for i in `cat ${FILENAME}`
    do
        echo $i
    done
}

function countLines(){
    exp="第\s*[0-9]+\s*章"
    
    while read LINE
    do
        result=$(echo ${LINE} | grep -P -o "${exp}")
        if [[ -n $result ]]; then
            echo "$result"
        fi
        
    done < $FILENAME
}

if [[ $# -lt 1 ]]; then
    usage
fi

countLines

echo -e "\n start file processing of each method\n"