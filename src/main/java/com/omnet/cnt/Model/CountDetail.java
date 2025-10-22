package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CountDetail {

	 @Id
	 
	 
	    private Integer io_viol_cnt;
	    private Integer io_prog_cnt;
	    private Integer io_arrest_inc;
	    private Integer io_admin_warr;
		public Integer getIo_viol_cnt() {
			return io_viol_cnt;
		}
		public void setIo_viol_cnt(Integer io_viol_cnt) {
			this.io_viol_cnt = io_viol_cnt;
		}
		public Integer getIo_prog_cnt() {
			return io_prog_cnt;
		}
		public void setIo_prog_cnt(Integer io_prog_cnt) {
			this.io_prog_cnt = io_prog_cnt;
		}
		public Integer getIo_arrest_inc() {
			return io_arrest_inc;
		}
		public void setIo_arrest_inc(Integer io_arrest_inc) {
			this.io_arrest_inc = io_arrest_inc;
		}
		public Integer getIo_admin_warr() {
			return io_admin_warr;
		}
		public void setIo_admin_warr(Integer io_admin_warr) {
			this.io_admin_warr = io_admin_warr;
		}
		public CountDetail(Integer io_viol_cnt, Integer io_prog_cnt, Integer io_arrest_inc, Integer io_admin_warr) {
			super();
			this.io_viol_cnt = io_viol_cnt;
			this.io_prog_cnt = io_prog_cnt;
			this.io_arrest_inc = io_arrest_inc;
			this.io_admin_warr = io_admin_warr;
		}
	    
}
