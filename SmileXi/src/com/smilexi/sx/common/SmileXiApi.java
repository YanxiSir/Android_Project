package com.smilexi.sx.common;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;

import org.ksoap2.serialization.SoapObject;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sea_monster.core.exception.InternalException;
import com.sea_monster.core.exception.ParseException;
import com.smilexi.sx.app.AppManager;
import com.smilexi.sx.common.net.CWebService;
import com.smilexi.sx.common.net.CWebService.WebHandlerCallBack;
import com.smilexi.sx.finals.ServerFinals;
import com.smilexi.sx.protocol.SxAnswers;
import com.smilexi.sx.protocol.SxAttentioned;
import com.smilexi.sx.protocol.SxDynamicReply;
import com.smilexi.sx.protocol.SxDynamics;
import com.smilexi.sx.protocol.SxNoResultDefault;
import com.smilexi.sx.protocol.SxNotiReply;
import com.smilexi.sx.protocol.SxNotiZan;
import com.smilexi.sx.protocol.SxQuestionType;
import com.smilexi.sx.protocol.SxQuestions;
import com.smilexi.sx.protocol.SxStatus;
import com.smilexi.sx.protocol.SxUpdateUserInfoReturn;
import com.smilexi.sx.protocol.SxUserBaseInfo;
import com.smilexi.sx.protocol.SxUserMainPageInfo;
import com.smilexi.sx.protocol.parser.GsonParser;
import com.smilexi.sx.request.domain.AskQuestionRequest;
import com.smilexi.sx.request.domain.GetAttentionedRequest;
import com.smilexi.sx.request.domain.NotiRequest;
import com.smilexi.sx.request.domain.ReplyAnswerRequest;
import com.smilexi.sx.request.domain.SubmitAnswerRequest;
import com.smilexi.sx.request.domain.SubmitReplyRequest;
import com.smilexi.sx.request.domain.UserGetDynamicsRequest;
import com.smilexi.sx.request.domain.UserImproveInfoRequest;
import com.smilexi.sx.request.domain.UserLoginRequest;
import com.smilexi.sx.request.domain.UserPublishDynamic;
import com.smilexi.sx.request.domain.UserRegisterRequest;
import com.smilexi.sx.request.domain.UserUpdateInfoRequest;
import com.smilexi.sx.request.domain.ZanDynamic;
import com.smilexi.sx.util.L;

import android.content.Context;
import android.text.TextUtils;

public class SmileXiApi {

	public Context mContext;

	public SmileXiApi(Context mContext) {
		this.mContext = mContext;
	}

	/*
	 * 获取dynamic zan noti
	 */
	public void getNotiZan(Object obj, final IApiCallback callback) {

		NotiRequest params = (NotiRequest) obj;
		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_GET_NOTI_ZAN);

		request.addProperty("uid", params.getUid());
		request.addProperty("sid", params.getSid());
		request.addProperty("count", params.getCount());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.GET_NOTI_OPERATE, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							L.d(result); 
							Type type = new TypeToken<SxStatus<SxNotiZan>>() {
							}.getType();
							GsonParser<SxStatus<SxNotiZan>> parser = new GsonParser<SxStatus<SxNotiZan>>(
									type);

							try {
								SxStatus<SxNotiZan> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}
	/*
	 * 获取dynamicreply noti
	 */
	public void getNotiReply(Object obj, final IApiCallback callback) {

		NotiRequest params = (NotiRequest) obj;
		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_GET_NOTI_REPLY);

		request.addProperty("uid", params.getUid());
		request.addProperty("sid", params.getSid());
		request.addProperty("count", params.getCount());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.GET_NOTI_OPERATE, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							L.d(result); 
							Type type = new TypeToken<SxStatus<SxNotiReply>>() {
							}.getType();
							GsonParser<SxStatus<SxNotiReply>> parser = new GsonParser<SxStatus<SxNotiReply>>(
									type);

							try {
								SxStatus<SxNotiReply> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 搜索user
	 */
	public void searchUser(int uid, String key, final IApiCallback callback) {

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_SEARCH_USER);

		request.addProperty("uid", uid);
		request.addProperty("key", key);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.SEARCH_OPERATE, request, new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxAttentioned>>() {
							}.getType();
							GsonParser<SxStatus<SxAttentioned>> parser = new GsonParser<SxStatus<SxAttentioned>>(
									type);

							try {
								SxStatus<SxAttentioned> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 获取收藏的答案
	 */
	public void getCollectedAnswers(int uid, final IApiCallback callback) {

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_GET_COLLECTED_ANSWER);

		request.addProperty("uid", uid);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.GET_DYNAMIC_REPLY, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxAnswers>>() {
							}.getType();
							GsonParser<SxStatus<SxAnswers>> parser = new GsonParser<SxStatus<SxAnswers>>(
									type);

							try {
								SxStatus<SxAnswers> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 得到某个答案的回复内容
	 */
	public void getAnswerReplys(int aid, final IApiCallback callback) {

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_GET_ANSWER_REPLY);

		request.addProperty("aid", aid);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.GET_DYNAMIC_REPLY, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxDynamicReply>>() {
							}.getType();
							GsonParser<SxStatus<SxDynamicReply>> parser = new GsonParser<SxStatus<SxDynamicReply>>(
									type);

							try {
								SxStatus<SxDynamicReply> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 回复某个答案
	 */
	public void replyAnswer(Object obj, final IApiCallback callback) {
		ReplyAnswerRequest params = (ReplyAnswerRequest) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_REPLY_ANSWER);

		request.addProperty("aid", params.getAid());
		request.addProperty("fuid", params.getFuid());
		request.addProperty("tuid", params.getTuid());
		request.addProperty("content", params.getContent());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.SEND_DYNAMIC_REPLY, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 收藏答案
	 */
	public void collectAnswer(int uid, int aid, final IApiCallback callback) {

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_COLLECT_ANSWER);

		request.addProperty("uid", uid);
		request.addProperty("aid", aid);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.DIANZAN_DYNAMIC, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * zanAnswer
	 */
	public void zanAnswer(int uid, int aid, final IApiCallback callback) {

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_ZAN_ANSWER);

		request.addProperty("uid", uid);
		request.addProperty("aid", aid);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.DIANZAN_DYNAMIC, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 关注问题
	 */
	public void attentionQuestion(int qid, int uid, final IApiCallback callback) {

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_ATTENTION_QUESTION);

		request.addProperty("qid", qid);
		request.addProperty("uid", uid);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.ATTENTION_OPERATE_QUESTION, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 提交回答
	 */
	public void submitAnswer(Object obj, final IApiCallback callback) {

		SubmitAnswerRequest params = (SubmitAnswerRequest) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_SUBMIT_ANSWER);

		request.addProperty("qid", params.getQid());
		request.addProperty("uid", params.getUid());
		request.addProperty("content", params.getContent());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.SEND_DYNAMIC_REPLY, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 得到某个问题的答案
	 */
	public void getQuestionAnswers(int uid, int qid, final IApiCallback callback) {

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_GET_QUESTION_ANSWERS);

		request.addProperty("uid", uid);
		request.addProperty("qid", qid);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.GET_DYNAMIC_REPLY, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxAnswers>>() {
							}.getType();
							GsonParser<SxStatus<SxAnswers>> parser = new GsonParser<SxStatus<SxAnswers>>(
									type);

							try {
								SxStatus<SxAnswers> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 获取问题列表
	 */
	public void getQuestion(Object obj, final IApiCallback callback) {

		UserGetDynamicsRequest params = (UserGetDynamicsRequest) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_GET_QUESTION);

		request.addProperty("spos", params.getStartId());
		request.addProperty("count", params.getCount());
		request.addProperty("oid", params.getOid());
		request.addProperty("uid", params.getUid());
		request.addProperty("wid", params.getWid());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.GET_DYNAMIC, request, new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxQuestions>>() {
							}.getType();
							GsonParser<SxStatus<SxQuestions>> parser = new GsonParser<SxStatus<SxQuestions>>(
									type);

							try {
								SxStatus<SxQuestions> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 获取问题类型列表
	 */
	public void getQuestionTypeList(Object obj, final IApiCallback callback) {

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_GET_QUESTION_TYPE);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.GET_QUESTION_TYPE, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxQuestionType>>() {
							}.getType();
							GsonParser<SxStatus<SxQuestionType>> parser = new GsonParser<SxStatus<SxQuestionType>>(
									type);

							try {
								SxStatus<SxQuestionType> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * ask question
	 */
	public void askQuestion(Object obj, final IApiCallback callback) {

		AskQuestionRequest params = (AskQuestionRequest) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_ASK_QUESTION);

		request.addProperty("uid", params.getUid());
		request.addProperty("type", params.getType());
		request.addProperty("title", params.getTitle());
		request.addProperty("content", params.getContent());
		request.addProperty("keyword", params.getKeyword());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.PUBLISH_DYNAMIC, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 获取关注的人 列表
	 */
	public void getAttentioned(Object obj, final IApiCallback callback) {

		GetAttentionedRequest params = (GetAttentionedRequest) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_GET_ALL_ATTENTIONED);

		request.addProperty("uid", params.getUid());
		request.addProperty("sid", params.getSid());
		request.addProperty("count", params.getCount());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.ATTENTION_OPERATE, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxAttentioned>>() {
							}.getType();
							GsonParser<SxStatus<SxAttentioned>> parser = new GsonParser<SxStatus<SxAttentioned>>(
									type);

							try {
								SxStatus<SxAttentioned> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 关注操作
	 */
	public void attentionUser(int fuid, int tuid, final IApiCallback callback) {

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_ATTENTION_USER);

		request.addProperty("fuid", fuid);
		request.addProperty("tuid", tuid);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.ATTENTION_OPERATE, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

	}

	/*
	 * 提交回复
	 */
	public void submitReply(Object obj, final IApiCallback callback) {
		SubmitReplyRequest params = (SubmitReplyRequest) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_SUBMIT_REPLY);

		request.addProperty("did", params.getDid());
		request.addProperty("content", params.getContent());
		request.addProperty("fuid", params.getFuid());
		request.addProperty("tuid", params.getTuid());
		request.addProperty("funame", params.getFuname());
		request.addProperty("tuname", params.getTuname());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.SEND_DYNAMIC_REPLY, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

	}

	/*
	 * 获取动态回复内容
	 */
	public void getDynamicReply(Object obj, final IApiCallback callback) {
		int did = (Integer) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_GET_REPLY);

		request.addProperty("did", did);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.GET_DYNAMIC_REPLY, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							L.d(result);
							Type type = new TypeToken<SxStatus<SxDynamicReply>>() {
							}.getType();
							GsonParser<SxStatus<SxDynamicReply>> parser = new GsonParser<SxStatus<SxDynamicReply>>(
									type);

							try {
								SxStatus<SxDynamicReply> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

	}

	/*
	 * 点赞
	 */
	public void zanDynamic(Object obj, final IApiCallback callback) {
		ZanDynamic params = (ZanDynamic) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_DIAN_ZAN);

		request.addProperty("uid", params.getUid());
		request.addProperty("did", params.getDid());
		request.addProperty("oid", params.getOid());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.DIANZAN_DYNAMIC, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

	}

	/*
	 * 删除动态
	 */
	public void delDynamic(Object obj, final IApiCallback callback) {
		int did = (Integer) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_DEL_DYNAMIC);

		request.addProperty("did", did);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.DEL_OPERATE, request, new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 获取指定用户基本信息
	 */
	public void getUserMainPage(int wid, int uid, final IApiCallback callback) {

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_GET_USER_INFO);

		request.addProperty("wid", wid);
		request.addProperty("uid", uid);

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.GET_USER_MAINPAGE, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxUserMainPageInfo>>() {
							}.getType();
							GsonParser<SxStatus<SxUserMainPageInfo>> parser = new GsonParser<SxStatus<SxUserMainPageInfo>>(
									type);

							try {
								SxStatus<SxUserMainPageInfo> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 获取动态信息
	 */
	public void getDynamics(Object obj, final IApiCallback callback) {
		UserGetDynamicsRequest params = (UserGetDynamicsRequest) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_GET_DYNAMIC);

		request.addProperty("spos", params.getStartId());
		request.addProperty("count", params.getCount());
		request.addProperty("oid", params.getOid());
		request.addProperty("uid", params.getUid());
		request.addProperty("wid", params.getWid());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.GET_DYNAMIC, request, new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxDynamics>>() {
							}.getType();
							GsonParser<SxStatus<SxDynamics>> parser = new GsonParser<SxStatus<SxDynamics>>(
									type);

							try {
								SxStatus<SxDynamics> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 发布生活动态
	 */
	public void publishDynamic(Object obj, final IApiCallback callback) {
		UserPublishDynamic params = (UserPublishDynamic) obj;
		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_PUBLISH_DYNAMIC);
		request.addProperty("uid", params.getUid());
		request.addProperty("typ", params.getTyp());
		request.addProperty("content", params.getContent());
		// request.addProperty("por", (String[])params.getPor());

		if (params.getPor() != null) {
			for (int i = 0; i < params.getPor().length; i++) {
				String s = TextUtils.isEmpty(params.getPor()[i]) ? "" : params
						.getPor()[i];
				request.addProperty("img" + (i + 1), s);
			}
			for (int i = params.getPor().length; i < 6; i++) {
				request.addProperty("img" + (i + 1), "");
			}
		} else {
			for (int i = 0; i < 6; i++) {
				request.addProperty("img" + (i + 1), "");
			}
		}

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.PUBLISH_DYNAMIC, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 更新用户信息
	 */
	public void updateUserInfo(Object obj, final IApiCallback callback) {
		UserUpdateInfoRequest params = (UserUpdateInfoRequest) obj;
		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.MESHOD_UPDATE_INFO);
		request.addProperty("oid", params.getOid());
		request.addProperty("content", params.getContent());
		request.addProperty("uid", params.getUid());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.CHANGE_SELF_INFO, request,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxUpdateUserInfoReturn>>() {
							}.getType();
							GsonParser<SxStatus<SxUpdateUserInfoReturn>> parser = new GsonParser<SxStatus<SxUpdateUserInfoReturn>>(
									type);

							try {
								SxStatus<SxUpdateUserInfoReturn> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 完善个人基本信息
	 */
	public void uploadUserImproveInfo(Object obj, final IApiCallback callback) {
		UserImproveInfoRequest params = (UserImproveInfoRequest) obj;
		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_UPLOAD_INFO);
		request.addProperty("uid", params.getUid());
		request.addProperty("username", params.getUsername());
		request.addProperty("signStr", params.getSignStr());
		request.addProperty("email", params.getEmail());
		request.addProperty("realName", params.getRealName());
		request.addProperty("genderId", params.getGenderId());
		request.addProperty("portrait", params.getPortrait());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.IMPROVE_INFO, request, new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxUserBaseInfo>>() {
							}.getType();
							GsonParser<SxStatus<SxUserBaseInfo>> parser = new GsonParser<SxStatus<SxUserBaseInfo>>(
									type);

							try {
								SxStatus<SxUserBaseInfo> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 注册获取验证码
	 */
	public void getRegisterVCode(Object obj, final IApiCallback callback) {
		String uPhone = (String) obj;
		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_REGISTER_VCODE);
		request.addProperty("uPhone", uPhone);
		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.GET_VCODE, request, new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == null)
									return;
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(String
											.valueOf(tmpResult.getResultcode()));
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

	}

	/*
	 * 用户注册
	 */
	public void userRegister(Object obj, final IApiCallback callback) {
		UserRegisterRequest params = (UserRegisterRequest) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_REGISTER);
		request.addProperty("userPhone", params.getUserPhone());
		request.addProperty("password", params.getPassword());
		request.addProperty("vCode", params.getvCode());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.REGISTER, request, new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxNoResultDefault>>() {
							}.getType();
							GsonParser<SxStatus<SxNoResultDefault>> parser = new GsonParser<SxStatus<SxNoResultDefault>>(
									type);

							try {
								SxStatus<SxNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 用户登录
	 */
	public void getUserLoginInfo(Object obj, final IApiCallback callback) {

		UserLoginRequest params = (UserLoginRequest) obj;

		SoapObject request = new SoapObject(ServerFinals.NAME_SPACE,
				ServerFinals.METHOD_USER_LOGIN);
		request.addProperty("userPhone", params.getUserPhone());
		request.addProperty("password", params.getPassword());

		CWebService.reqSessionHandler(AppManager.getInstance(),
				ServerFinals.LOGIN, request, new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<SxStatus<SxUserBaseInfo>>() {
							}.getType();
							GsonParser<SxStatus<SxUserBaseInfo>> parser = new GsonParser<SxStatus<SxUserBaseInfo>>(
									type);

							try {
								SxStatus<SxUserBaseInfo> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

	}

	public interface IApiCallback {
		void onError(int errorCode);

		void onComplete(Object object);
	}

}
