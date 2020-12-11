APP_NAME="kafka_spring"
APP_VERSION="1.0-SNAPSHOT"
JAVA_PARAM="-Xmx1g"

BIN_PATH=$TWM_HOME_PARENT/Kafka/$APP_NAME/bin
JAR_PATH=$BIN_PATH/../target/$APP_NAME-$APP_VERSION.jar

echo "Starting '$APP_NAME' with java param: '$JAVA_PARAM', at '$JAR_PATH'"
java $JAVA_PARAM -jar $JAR_PATH