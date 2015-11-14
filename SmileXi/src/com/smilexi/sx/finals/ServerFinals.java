package com.smilexi.sx.finals;

public class ServerFinals {

	public static int USER_BIGIMG = 200;

	public static final String WS_DOMAIN = "http://192.168.137.1/";

	public static final String NAME_SPACE = "http://api.hibernate.smilexi.com";

	// private final static String H =
	// "http://192.168.137.1:8080/axis2/services/smilexi/";

	private final static String H = "http://192.168.137.1:8080/WebServiceProject/services/";
	public final static String HP = "http://192.168.137.1:8080/SmileXiServer/HeadImage/";
	public final static String HDP = "http://192.168.137.1:8080/SmileXiServer/DynamicPics/";

	// private final static String H =
	// "http://58.154.189.114:8080/WebServiceProject/services/";
	// public final static String HP =
	// "http://58.154.189.114:8080/SmileXiServer/HeadImage/";
	// public final static String HDP =
	// "http://58.154.189.114:8080/SmileXiServer/DynamicPics/";

	public final static String LOGIN = H + "Login?wsdl";
	public final static String REGISTER = H + "Register?wsdl";
	public final static String GET_VCODE = H + "GetVCode?wsdl";
	public final static String IMPROVE_INFO = H + "ImproveInfo?wsdl";
	public final static String CHANGE_SELF_INFO = H + "ChangeUserInfo?wsdl";
	public final static String PUBLISH_DYNAMIC = H + "PublishDynamic?wsdl";
	public final static String GET_DYNAMIC = H + "GetDynamic?wsdl";
	public final static String GET_USER_MAINPAGE = H + "GetUserMainPage?wsdl";
	public final static String DEL_OPERATE = H + "DelOperate?wsdl";
	public final static String DIANZAN_DYNAMIC = H + "DianZanDynamic";
	public final static String GET_DYNAMIC_REPLY = H + "GetDynamicReply?wsdl";
	public final static String SEND_DYNAMIC_REPLY = H + "SendDynamicReply?wsdl";
	public final static String ATTENTION_OPERATE = H + "AttentionUser?wsdl";
	public final static String ATTENTION_OPERATE_QUESTION = H
			+ "AttentionOperate?wsdl";
	public final static String SEARCH_OPERATE = H + "SearchOperate?wsdl";
	public final static String GET_QUESTION_TYPE = H + "GetQuestionType?wsdl";
	public final static String GET_NOTI_OPERATE = H
			+ "NotifacationOperate?wsdl";

	/*
	 * method方法
	 */
	public static final String METHOD_USER_LOGIN = "userLogin";
	public static final String METHOD_REGISTER_VCODE = "getRegisterVCode";
	public static final String METHOD_REGISTER = "userRegister";
	public static final String METHOD_UPLOAD_INFO = "uploadInfo";
	public static final String MESHOD_UPDATE_INFO = "updateInfo";
	public static final String METHOD_PUBLISH_DYNAMIC = "publishDynamic";
	public static final String METHOD_GET_DYNAMIC = "getDynamicInfo";
	public static final String METHOD_GET_USER_INFO = "getUserInfo";
	public static final String METHOD_DEL_DYNAMIC = "delDynamic";
	public static final String METHOD_DIAN_ZAN = "dianzan";
	public static final String METHOD_GET_REPLY = "getReply";
	public static final String METHOD_SUBMIT_REPLY = "submitReply";
	public static final String METHOD_ATTENTION_USER = "attention";
	public static final String METHOD_GET_ALL_ATTENTIONED = "getAllAttentoned";

	/*
	 * question相关
	 */
	public static final String METHOD_ASK_QUESTION = "askQuestion";// publishdynamic
	public static final String METHOD_GET_QUESTION = "getQuestion";// getDynamic
	public static final String METHOD_GET_QUESTION_ANSWERS = "getQuestionAnswers";// GetDynamicReply
	public static final String METHOD_SUBMIT_ANSWER = "submitAnswer";// SendDynamicReply
	public static final String METHOD_ATTENTION_QUESTION = "attentionQuestion";
	public static final String METHOD_ZAN_ANSWER = "zanAnswer";// DianZanDynamic
	public static final String METHOD_COLLECT_ANSWER = "collectAnswer";// DianZanDynamic

	public static final String METHOD_REPLY_ANSWER = "replyAnswer";// SendDynamicReply
	public static final String METHOD_GET_ANSWER_REPLY = "getAnswerReplys";// GetDynamicReply
	public static final String METHOD_GET_COLLECTED_ANSWER = "getCollectedAnswer";// GetDynamicReply
	public static final String METHOD_GET_QUESTION_TYPE = "getQuetionType";// GetQuestionType

	/*
	 * search
	 */
	public static final String METHOD_SEARCH_USER = "searchUser";// SearchOperate

	/*
	 * notifacation
	 */
	public static final String METHOD_GET_NOTI_REPLY = "getReply";// NotifacationOperate
	public static final String METHOD_GET_NOTI_ZAN = "getZanDynamic";// NotifacationOperate

}
