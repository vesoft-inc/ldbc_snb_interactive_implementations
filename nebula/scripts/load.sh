#!/bin/bash

set -eu
# Directory of this script
SCRIPT_DIR=$(dirname $(readlink -f "$0"))
# Directory of this project
PROJECT_DIR=$(dirname ${SCRIPT_DIR})

source ${SCRIPT_DIR}/env.sh

for i in generate.sh convert.sh convert_person.sh convert_post_comment.sh;do
	${SCRIPT_DIR}/$i
	if [ $? -ne 0 ];then
		echo "ERROR: $i failed"
		exit 1
	fi
done

cd ${SCRIPT_DIR}
# replace nebula, space and data path
# {{graph_addresses}}
# {{space_name}}
sed "s/{{space_name}}/${NEBULA_SPACE}/g" importer_template.yaml > importer_${NEBULA_SPACE}.yaml

sed -i "s/{{graph_addresses}}/${NEBULA_ADDRESS}/g" importer_${NEBULA_SPACE}.yaml
data_dir=${LDBC_HOME}/test_data
sed -i "s#{{data_folder}}#${data_dir}#g" importer_${NEBULA_SPACE}.yaml

if [ -x ${SCRIPT_DIR}/nebula-importer ];then
  echo "nebula-importer: ${SCRIPT_DIR}/nebula-importer is existed, ignore download"
else
	curl -L "http://github.com/vesoft-inc/nebula-importer/releases/download/${NEBULA_IMPORTER_VERSION}/nebula-importer-linux-amd64-${NEBULA_IMPORTER_VERSION}" \
	-o ${SCRIPT_DIR}/nebula-importer
	chmod +x ${SCRIPT_DIR}/nebula-importer
fi

# import data
${SCRIPT_DIR}/nebula-importer -config importer_${NEBULA_SPACE}.yaml
