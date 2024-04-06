package srcs.webservices;

public interface SRCSWebService {

	public String getName();
	public void deploy() throws Exception;
	
	public void undeploy() throws Exception;
}
