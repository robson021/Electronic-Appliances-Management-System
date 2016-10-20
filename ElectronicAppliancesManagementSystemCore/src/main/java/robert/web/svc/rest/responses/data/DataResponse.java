package robert.web.svc.rest.responses.data;

abstract class DataResponse {

	private boolean result = false;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}
