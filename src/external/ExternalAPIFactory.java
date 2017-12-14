package external;

public class ExternalAPIFactory {
	private static final String DEFAULT_SOURCE = "TMS";
	/**
	 * get ExternalAPI in Factory Model
	 * @return
	 */
	public static ExternalAPI getExternalAPI() {
		return getExternalAPI(DEFAULT_SOURCE);
	}
	
	/**
	 * create different APIs based on the Source name 
	 * @param name
	 * @return
	 */
	public static ExternalAPI getExternalAPI(String name) {
		switch(name) {
		case "TMS":
			return new TMSAPI();
		case "TMDB":
			return null;
		case "OMDB":
			return null;
		default:
			throw new IllegalArgumentException("invailid name" + name);
		}
	}
}
