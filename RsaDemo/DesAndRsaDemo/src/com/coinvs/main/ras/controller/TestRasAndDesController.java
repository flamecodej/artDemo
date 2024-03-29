package com.coinvs.main.ras.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coinvs.main.des_and_rsa_and_md5.DES;
import com.coinvs.main.des_and_rsa_and_md5.MD5;
import com.coinvs.main.des_and_rsa_and_md5.RSA;
import com.coinvs.main.status.entity.StatusCode;
import com.coinvs.main.status.entity.TotalNote;

/**
 * test encryption
 * @author water
 *
 */
@Controller
public class TestRasAndDesController {
	
	/**
	 * 请求url: http://localhost:8080/DesAndRsaDemo/testRsa.do?ciphertext=........
	 * @param ciphertext
	 * @return
	 */
	@RequestMapping("testRsa.do")
	@ResponseBody
	public TotalNote testRas(String ciphertext){
		System.out.println("收到请求！");
		TotalNote note = new TotalNote();
		try{
			System.out.println(ciphertext);
			String decode = RSA.decryptByPrivateKey(ciphertext);
			System.out.println("解密后的字符串："+decode); 
			note.setError_code(StatusCode.SUCCESS_CODE);
			note.setData(decode);
		}catch(Exception e){
			e.printStackTrace();
		}  
		/*String data = "Hello World！";
		note.setError_code(StatusCode.SUCCESS_CODE);
		note.setData(decode);*/
		return note;
	}
	
	/**
	 * 测试DES加密：
	 * 请求url: http://localhost:8080/DesAndRsaDemo/testDes.do?ciphertext=.........
	 */
	@RequestMapping("testDes.do")
	@ResponseBody
	public TotalNote testDes(String ciphertext){
		System.out.println("收到请求！");
		TotalNote note = new TotalNote();
		try{
			System.out.println(ciphertext);
			String decode = DES.encryptDES(ciphertext, DES.PASSWORD_CRYPT_KEY);
			System.out.println("解密后的字符串："+decode); 
		}catch(Exception e){
			e.printStackTrace();
		}
		String data = "Hello World！";
		note.setError_code(StatusCode.SUCCESS_CODE);
		note.setData(data);
		return note;
	}
	
	/**
	 * 将Des和Rsa结合使用：将desKey及关键字段（用户id）使用rsa公钥加密，传到服务器
	 * 服务器使用rsa私钥解密，获得desKey及用户id，并根据id查找用户余额，使用des加密后返回给客户端
	 * @param userId 用户id
	 * @param desKey客户端传来的des加密的key
	 * @return 用户的一些关键信息
	 */
	@RequestMapping("testRsaAndDes.do")
	@ResponseBody
	public TotalNote testRsaAndDes(String userId,String desKey){
		System.out.println("收到请求！");
		TotalNote note = new TotalNote();
		try{
			System.out.println(userId);
			//使用私钥解密
			userId = RSA.decryptByPrivateKey(userId);
			desKey = RSA.decryptByPrivateKey(desKey);
			
			//查找数据库获取用户，账户余额等关键字段，使用des进行加密
			String account = "10000.0";
			account = DES.encryptDES(account, desKey);
			
			note.setError_code(StatusCode.SUCCESS_CODE);
			note.setError_message("获取数据成功！");
			note.setData(account);
			return note;
		}catch(Exception e){
			e.printStackTrace();
			note.setError_code(StatusCode.ERROR_CODE);
			note.setError_message(StatusCode.ERROR_MESSAGE);
			return note;
		}  
	}
	
	/**
	 * 测试MD5加密
	 * 请求url：http://localhost:8080/DesAndRsaDemo/testMd5.do?password=..........&md5=..........
	 * @param md5
	 * @return
	 */
	@RequestMapping("testMd5.do")
	@ResponseBody
	public TotalNote testMd5(String password,String md5){
		System.out.println("收到请求！");
		TotalNote note = new TotalNote();
		try{
			System.out.println("使用md5加密后的:"+md5);
			String md5Str = MD5.getMD5Code(password);
			if(md5Str.equals(md5)){
				System.out.println("密码验证成功！");
			}else{
				System.out.println("密码验证失败！");
			}
		}catch(Exception e){
			e.printStackTrace();
		}  
		String data = "Hello World！";
		note.setError_code(StatusCode.SUCCESS_CODE);
		note.setData(data);
		return note;
	}

}
