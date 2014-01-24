package eye

public class Main {

	public static void main(String[] args) {
		def tracker = new Tracker()
		def ui = new UI()
		tracker.listeners << ui 
		tracker.connect()

	}

}
