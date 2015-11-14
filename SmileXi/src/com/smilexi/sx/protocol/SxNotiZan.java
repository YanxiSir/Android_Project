package com.smilexi.sx.protocol;

import java.io.Serializable;
import java.util.List;

import com.smilexi.sx.finals.ServerFinals;
import com.smilexi.sx.protocol.SxDynamics.Dynamic;

public class SxNotiZan implements Serializable {
	private List<NotiZan> notiZans;

	public List<NotiZan> getNotiZans() {
		return notiZans;
	}

	public void setNotiZans(List<NotiZan> notiZans) {
		this.notiZans = notiZans;
	}

	public static class NotiZan implements Serializable {
		private Dynamic dynamic;
		private int fuid;
		private String fName;
		private String fPortrait;
		private String zanTime;

		public Dynamic getDynamic() {
			return dynamic;
		}

		public void setDynamic(Dynamic dynamic) {
			this.dynamic = dynamic;
		}

		public int getFuid() {
			return fuid;
		}

		public void setFuid(int fuid) {
			this.fuid = fuid;
		}

		public String getfName() {
			return fName;
		}

		public void setfName(String fName) {
			this.fName = fName;
		}

		public String getfPortrait() {
			return ServerFinals.HP + fPortrait;
		}

		public void setfPortrait(String fPortrait) {
			this.fPortrait = fPortrait;
		}

		public String getZanTime() {
			return zanTime;
		}

		public void setZanTime(String zanTime) {
			this.zanTime = zanTime;
		}

	}

}
