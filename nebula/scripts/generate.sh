#!/bin/bash

set -eu
# generate the LDBC data.

# Directory of this script
SCRIPT_DIR=$(dirname $(readlink -f "$0"))
# Directory of this project
PROJECT_DIR=$(dirname ${SCRIPT_DIR})
source ${PROJECT_DIR}/scripts/env.sh

echo "==============================================================================="
echo "Generating the LDBC data"

mkdir -p ${DATA_DIR}

if [ -d ${HADOOP_HOME} ];then
  echo "Hadoop: ${HADOOP_HOME} is existed, ignore download"
else
  echo "Download hadoop"
  cd ${DATA_DIR} && \
  wget -c   http://archive.apache.org/dist/hadoop/core/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz 
  echo "extract hadoop files"
  tar zxvf hadoop-${HADOOP_VERSION}.tar.gz -C ${DATA_DIR} >  /dev/null
  echo "extract hadoop files done"

fi

if [ -d ${LDBC_HOME} ];then
  echo "ldbc_snb_datagen: ${LDBC_HOME} is existed, ignore download"
else
cd ${DATA_DIR}  && \
rm -rf ldbc_snb_datagen && \
git clone --branch ${LDBC_SNB_DATAGEN_VERSION} https://github.com/ldbc/ldbc_snb_datagen && \
cd ldbc_snb_datagen  && \
cp test_params.ini params.ini
fi

echo "generate data"

cd ${LDBC_HOME} && \
# need modify the `scaleFactor` of ldbc_snb
sed -i "s/interactive.*/interactive.${SCALE_FACTOR}/g" params.ini && \
# datetime format
sed -i "s/ldbc.snb.datagen.util.formatter.StringDateFormatter.dateTimeFormat.*//g" params.ini && \
echo "ldbc.snb.datagen.util.formatter.StringDateFormatter.dateTimeFormat:yyyy-MM-dd'T'HH:mm:ss.SSS" >> params.ini && \
echo "ldbc.snb.datagen.parametergenerator.python:python2" >> params.ini

export HADOOP_HOME=${HADOOP_HOME}

# need python2 to generate parameters
alias python=python2
LDBC_SNB_DATAGEN_HOME=${LDBC_HOME}  && \
./run.sh 
echo "LDDBC data is generated in ${LDBC_HOME}/test_data/"
