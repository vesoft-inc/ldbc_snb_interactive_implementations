#!/bin/bash

set -eu
# convert
# 1. combine the speaks and email
# 2. sort person csv
# 3. append the emails and speaks to the person csv

# Directory of this script
SCRIPT_DIR=$(dirname $(readlink -f "$0"))
# Directory of this project
PROJECT_DIR=$(dirname ${SCRIPT_DIR})

source ${SCRIPT_DIR}/env.sh

cd ${LDBC_HOME}/test_data/social_network/dynamic

# 933|si
# 933|en
# =>
# 933|si,en
awk -F "|" '{a[$1]=a[$1]$2","}END{for(i in a){print i"|"substr(a[i], 0, length(a[i])-1)}}' person_speaks_language.csv \
|sort -n -t '|' -k 1 > person_speaks_language_new.csv

# 933|Mahinda933@boarderzone.com
# 933|Mahinda933@hotmail.com
# 933|Mahinda933@boarderzone.com,Mahinda933@hotmail.com
awk -F "|" '{a[$1]=a[$1]$2","}END{for(i in a){print i"|"substr(a[i], 0, length(a[i])-1)}}' person_email_emailaddress.csv \
|sort -n -t '|' -k 1 > person_email_emailaddress_new.csv

sort -n -t "|" -k 1 person.csv > person_new.csv

paste -d "|" person_new.csv person_email_emailaddress_new.csv person_speaks_language_new.csv  > person_final.csv
