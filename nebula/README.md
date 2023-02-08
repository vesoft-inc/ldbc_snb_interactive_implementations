## User's guide

1. Grab the `stable` driver source code from:

   ```bash
   git clone --branch stable https://github.com/ldbc/ldbc_snb_driver
   ```

1. Install the driver artifact to the local Maven repository:

   ```bash
   cd ldbc_snb_driver
   mvn clean install -DskipTests
   ```

2. Navigate to the root of this repository and build it to generate the JAR files for the implementations:

   ```bash
   ./build.sh
   ```

3. Load data into nebula graph

```
# by default, load the data in `127.0.0.1:9669` with space `sf0_1`.
./nebula/scripts/load.sh

# or pass the environments.

SCALE_FACTOR=10 \
NEBULA_ADDRESS=192.168.15.8:9669 \
NEBULA_SPACE=sf10 \
./nebula/scripts/load.sh

```

4. For each implementation, it is possible to (1) create validation parameters, (2) validate against an existing validation parameters, and (3) run the benchmark. Set the parameters according to your system configuration in the appropriate `.properties` file and run the driver with one of the following scripts:

   ```bash
   # Interactive workload - note that if the workload contains updates, the database needs to be re-loaded between steps
   # ./interactive-create-validation-parameters.sh
   # ./interactive-validate.sh
   ./interactive-benchmark.sh
   ```
   
   - configs
   ```bash
   endpoint=192.168.15.3:9669,192.168.15.5:9669,192.168.15.6:9669
   user=root
   password=nebula
   spaceName=sf300_033

   ## 路径都是基于执行路径，最好写绝对路径
   queryDir=../queries/
   ldbc.snb.interactive.parameters_dir=../test-data/substitution_parameters/
   ldbc.snb.interactive.updates_dir=../test-data/social_network/
   
   ldbc.snb.interactive.LdbcQuery10_enable=false
   ldbc.snb.interactive.LdbcQuery14_enable=false
   ```
