import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;
import com.lvtu.wechat.common.model.sys.User;
import com.lvtu.wechat.common.service.sys.UserService;


public class TestHessian {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		HessianProxyFactory factory = new HessianProxyFactory();  
        UserService userService =  (UserService) factory.create(UserService.class, "http://localhost:8080/remoting/remoteUserService");  
        User user = userService.getUserById("1");
        System.out.println(user);
	}

}
