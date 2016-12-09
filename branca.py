import paho.mqtt.client as mqtt
import MySQLdb

MASTER_ID = 111111;

def on_connect(client, userdata, rc):
    print("Connected with result code "+str(rc))
	# Subscribing in on_connect() means that if we lose the connection and
	# reconnect then subscriptions will be renewed.
    client.subscribe("/branca/#");

# cartao mestre 296057479
# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
	print(msg.topic+" "+str(msg.payload))

	if msg.topic.startswith("/branca/grupos"):
		print("Oi");
		db = MySQLdb.connect(host="localhost",    # your host, usually localhost
                     user="root",         # your username
                     passwd="root",  # your password
                     db="branca_chat")        # name of the data base
		# you must create a Cursor object. It will let
		#  you execute all the queries you need
		cur = db.cursor()

		sql = "SELECT * FROM grupos WHERE id = %s;"
		cur.execute(sql, msg.topic.split("/")[-1])

		if cur.rowcount == 0:
			print(msg.topic.split("/")[-1])
			sql = """INSERT INTO grupos (id)
			VALUES (%s)"""
			cur.execute(sql, (msg.topic.split("/")[-1]))
		db.commit()
		db.close()
	elif msg.topic.startswith("/branca/usuarios"):
		db = MySQLdb.connect(host="localhost",    # your host, usually localhost
                     user="root",         # your username
                     passwd="root",  # your password
                     db="branca_chat")        # name of the data base
		# you must create a Cursor object. It will let
		#  you execute all the queries you need
		cur = db.cursor()

		sql = "SELECT * FROM usuarios WHERE id = %s;"
		cur.execute(sql, msg.topic.split("/")[-1])

		if cur.rowcount == 0:
			sql = """INSERT INTO usuarios (id)
			VALUES (%s)"""
			cur.execute(sql, (msg.topic.split("/")[-1]))
		db.commit()
		db.close()

client = mqtt.Client(transport="websockets")
client.on_connect = on_connect
client.on_message = on_message

#client.connect("iot.oceanmanaus.com", 1883, 60)
client.connect("broker.mqttdashboard.com", 8000, 60)

# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
client.loop_forever()
