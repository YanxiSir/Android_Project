package com.smilexi.sx.protocol;

import java.io.Serializable;
import java.util.List;

import android.text.TextUtils;

import com.smilexi.sx.finals.ServerFinals;

public class SxDynamics implements Serializable {

	private List<Dynamic> dynamics;

	public List<Dynamic> getDynamics() {
		return dynamics;
	}

	public void setDynamics(List<Dynamic> dynamics) {
		this.dynamics = dynamics;
	}

	public static class Dynamic implements Serializable {
		private int did;
		private int uid;
		private String uportrait;
		private String uname;
		private String usign;
		private String dtime;
		private int dtyp;
		private String dcontent;
		private int replysize;
		private int iszan;
		private int zanscount;
		private List<Dynamic_Photos> dpics;

		
		
		public int getDid() {
			return did;
		}

		public void setDid(int did) {
			this.did = did;
		}

		public int getUid() {
			return uid;
		}

		public void setUid(int uid) {
			this.uid = uid;
		}

		public String getUportrait() {
			if (TextUtils.isEmpty(uportrait))
				return null;
			if (!uportrait.startsWith("http://"))
				return ServerFinals.HP + uportrait;
			return uportrait;
		}

		public void setUportrait(String uportrait) {
			this.uportrait = uportrait;
		}

		public String getUname() {
			return uname;
		}

		public void setUname(String uname) {
			this.uname = uname;
		}

		public String getUsign() {
			return usign;
		}

		public void setUsign(String usign) {
			this.usign = usign;
		}

		public String getDtime() {
			return dtime;
		}

		public void setDtime(String dtime) {
			this.dtime = dtime;
		}

		public int getDtyp() {
			return dtyp;
		}

		public void setDtyp(int dtyp) {
			this.dtyp = dtyp;
		}

		public String getDcontent() {
			return dcontent;
		}

		public void setDcontent(String dcontent) {
			this.dcontent = dcontent;
		}
		
		

		public int getReplysize() {
			return replysize;
		}

		public void setReplysize(int replysize) {
			this.replysize = replysize;
		}

		
		
		public int getIszan() {
			return iszan;
		}

		public void setIszan(int iszan) {
			this.iszan = iszan;
		}

		
		public int getZanscount() {
			return zanscount;
		}

		public void setZanscount(int zanscount) {
			this.zanscount = zanscount;
		}
		
		


		public List<Dynamic_Photos> getDpics() {
			return dpics;
		}

		public void setDpics(List<Dynamic_Photos> dpics) {
			this.dpics = dpics;
		}

		public static class Dynamic_Photos implements Serializable {
			private String dpic;

			public String getDpic() {
				if (TextUtils.isEmpty(dpic))
					return null;
				if (!dpic.startsWith("http://"))
					return ServerFinals.HDP + dpic;
				return dpic;
			}

			public void setDpic(String dpic) {
				this.dpic = dpic;
			}

		}

	}
}
