package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JUDGE_MST")
public class Judges {
	@Id
	@Column(name = "JUDGE_CODE")
	private String judgeCode;
	@Column(name = "JUDGE_FNAME")
	private String judgeFName;
	@Column(name = "JUDGE_LNAME")
	private String judgeLName;
	@Column(name = "JUDGE_MNAME")
	private String judgeMName;
	@Column(name = "JUDGE_SNAME")
	private String judgeSName;
	
	public String getJudgeCode() {
		return judgeCode;
	}
	public void setJudgeCode(String judgeCode) {
		this.judgeCode = judgeCode;
	}
	public String getJudgeFName() {
		return judgeFName;
	}
	public void setJudgeFName(String judgeFName) {
		this.judgeFName = judgeFName;
	}
	public String getJudgeLName() {
		return judgeLName;
	}
	public void setJudgeLName(String judgeLName) {
		this.judgeLName = judgeLName;
	}
	public String getJudgeMName() {
		return judgeMName;
	}
	public void setJudgeMName(String judgeMName) {
		this.judgeMName = judgeMName;
	}
	public String getJudgeSName() {
		return judgeSName;
	}
	public void setJudgeSName(String judgeSName) {
		this.judgeSName = judgeSName;
	}
}
