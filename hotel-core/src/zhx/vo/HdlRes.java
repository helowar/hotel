
package zhx.vo;

import com.mangocity.hdl.hotel.dto.MGExResult;


/**
 * 和各个合作方直连的接口返回结果类
 * 
 * @author chenkeming
 *
 */
public class HdlRes<T> {

    protected MGExResult result;

    protected T obj;

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	public MGExResult getResult() {
		return result;
	}

	public void setResult(MGExResult result) {
		this.result = result;
	}

}
