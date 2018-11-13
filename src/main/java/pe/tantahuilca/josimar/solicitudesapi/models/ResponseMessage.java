package pe.tantahuilca.josimar.solicitudesapi.models;

public class ResponseMessage {

	private String type;

	private String message;

	private ResponseMessage() {

	}

	public static ResponseMessage success(String message) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setType("success");
		responseMessage.setMessage(message);
		return responseMessage;
	}

	public static ResponseMessage error(String message) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setType("error");
		responseMessage.setMessage(message);
		return responseMessage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ResponseMessage [type=" + type + ", message=" + message + "]";
	}

}
