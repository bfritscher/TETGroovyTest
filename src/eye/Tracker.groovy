package eye
import groovy.json.JsonSlurper
import java.util.Timer.*

class Tracker {

	//api reference http://dev.theeyetribe.com/api/
	final String REQ_CONNECT = "{\"values\":{\"push\":true,\"version\":1},\"category\":\"tracker\",\"request\":\"set\"}"
	final String REQ_HEARTBEAT = "{\"category\":\"heartbeat\",\"request\":null}"
	final Integer HEARTBEAT = 300

	def socket = new Socket("localhost", 6555)
	def slurper = new JsonSlurper()
	def timer  = new Timer()
	def listeners = []

	Tracker(){

	}


	def connect(){
		Thread.start {
			socket.withStreams { inStream, outStream ->
				outStream << REQ_CONNECT
				timer.scheduleAtFixedRate(new TimerTask() {
							@Override
							public void run() {
								outStream << REQ_HEARTBEAT
							}
						}, HEARTBEAT, HEARTBEAT);
				def reader = inStream.newReader()
				def responseText
				while(responseText = reader.readLine()){
					def obj = slurper.parseText(responseText)
					if(obj.category == 'tracker' && obj.statuscode == 200){
						if(obj?.values?.frame){
							notifyAll(obj.values.frame)
						}
					}

				}
				timer.cancel()
				timer.purge()
			}
		}
	}

	void notifyAll(point){
		listeners.each{ listener ->
			listener.notify(point)
		}
	}
}
