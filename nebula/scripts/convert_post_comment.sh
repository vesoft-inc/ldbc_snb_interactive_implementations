#!/bin/bash

set -eu
# convert
# 1. combine the post.csv and post_hasCreator_person.csv
# 2. combine the comment.csv and comment_hasCreator_person.csv

# Directory of this script
SCRIPT_DIR=$(dirname $(readlink -f "$0"))
# Directory of this project
PROJECT_DIR=$(dirname ${SCRIPT_DIR})

source ${SCRIPT_DIR}/env.sh

cd ${LDBC_HOME}/test_data/social_network/dynamic

for n in post comment;do
	paste -d "|"  ${n}_hasCreator_person.csv ${n}.csv > ${n}_hasCreator_person_new.csv
done
