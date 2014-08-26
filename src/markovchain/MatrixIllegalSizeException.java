/**
 * 
 */
package markovchain;

/**
 * 
 * @author Dang Khoa Vo
 *
 */
class MatrixIllegalSizeException extends Exception {

	MatrixIllegalSizeException() {
		super();
	}

	@Override
	public String getMessage() {
		return "The matrix size dose not match for the operation.";
	}

}
