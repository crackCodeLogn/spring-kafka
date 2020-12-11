BASE_DIR=$KAFKA_LOCAL #Set in .bashrc

echo "Starting zookeper now!"
# START ZOOKEEPER first
ZOOKEEPER_START_CMD="$BASE_DIR/bin/zookeeper-server-start.sh $BASE_DIR/config/zookeeper.properties"
gnome-terminal -- bash -c "$ZOOKEEPER_START_CMD; exec bash"

sleep 5s
echo "Starting kafka broker now!"
#START KAFKA BROKER SERVICE NOW!
KAFKA_SERVER_START_CMD="$BASE_DIR/bin/kafka-server-start.sh $BASE_DIR/config/server.properties"
gnome-terminal -- bash -c "$KAFKA_SERVER_START_CMD; exec bash"
