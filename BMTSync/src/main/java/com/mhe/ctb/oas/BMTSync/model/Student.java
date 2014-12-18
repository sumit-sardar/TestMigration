package com.mhe.ctb.oas.BMTSync.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;



/**
 * A Student represented in JSON
 * 
 * @author cparis
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Student {
	private Integer _oasStudentId;
	private Integer _oasCustomerId;
	private String _studentusername;
	private String _firstName;
	private String _middleName;
	private String _lastName;
	private String _birthdate;
	private String _gender;
	private String _grade;
	private List<HierarchyNode> _heirarchySet;
	//private StudentAccomodation _accomodations;
	private String _customerStudentId;
	private Accomodations accomodations;
	//private int _errorCode;
	//private String _errorMessage; 
	
	public static class Accomodations {
		private String screen_magnifier;	
		private String screen_reader;
		private String calculator;
		private String test_pause;
		private String untimed_test;
		private String question_background_color;
		private String question_font_color;
		private String question_font_size;
		private String answer_background_color;
		private String answer_font_color;
		private String answer_font_size;
		private String highlighter;
		private String music_file_id;
		private String masking_ruler;
		private String magnifying_glass;
		private String extended_time;
		private String masking_tool;
		private String microphone_headphone;
		private float extended_time_factor;
		
		public String getScreen_Magnifier() {
			return screen_magnifier;
		}
		@JsonProperty(value="screen_magnifier", required=false)
		public void setScreen_Magnifier(String screen_Magnifier) {
			this.screen_magnifier = screen_Magnifier;
		}
		
		
		public String getScreen_Reader() {
			return screen_reader;
		}
		@JsonProperty(value="screen_reader", required=false)
		public void setScreen_Reader(String screen_reader) {
			this.screen_reader = screen_reader;
		}
		
		
		public String getCalculator() {
			return calculator;
		}
		@JsonProperty(value="calculator", required=false)
		public void setCalculator(String calculator) {
			this.calculator = calculator;
		}
		
		@JsonProperty(value="test_pause", required=false)
		public String getTest_Pause() {
			return test_pause;
		}
		public void setTest_Pause(String test_Pause) {
			this.test_pause = test_Pause;
		}
		
		
		public String getUntimed_Test() {
			return untimed_test;
		}
		@JsonProperty(value="untimed_test", required=false)
		public void setUntimed_Test(String untimed_Test) {
			this.untimed_test = untimed_Test;
		}
		
		
		public String getQuestion_background_color() {
			return question_background_color;
		}
		@JsonProperty(value="question_background_color", required=false)
		public void setQuestion_background_color(String question_BackGround_Color) {
			question_background_color = question_BackGround_Color;
		}
		
		public String getQuestion_font_color() {
			return question_font_color;
		}
		@JsonProperty(value="question_font_color", required=false)
		public void setQuestion_font_color(String question_Font_Color) {
			this.question_font_color = question_Font_Color;
		}

		public String getQuestion_font_size() {
			return question_font_size;
		}
		@JsonProperty(value="question_font_size", required=false)
		public void setQuestion_font_size(String question_Font_Size) {
			this.question_font_size = question_Font_Size;
		}
		
		
		public String getAnswer_background_color() {
			return answer_background_color;
		}
		@JsonProperty(value="answer_background_color", required=false)
		public void setAnswer_background_color(String answer_background_color) {
			this.answer_background_color = answer_background_color;
		}
		
		
		public String getAnswer_font_color() {
			return answer_font_color;
		}
		@JsonProperty(value="answer_font_color", required=false)
		public void setAnswer_font_color(String answer_font_color) {
			this.answer_font_color = answer_font_color;
		}
		
		
		public String getAnswer_font_size() {
			return answer_font_size;
		}
		@JsonProperty(value="answer_font_size", required=false)
		public void setAnswer_font_size(String answer_font_size) {
			this.answer_font_size = answer_font_size;
		}
		
		
		public String getHighlighter() {
			return highlighter;
		}
		@JsonProperty(value="highlighter", required=false)
		public void setHighlighter(String highlighter) {
			this.highlighter = highlighter;
		}
		
		
		public String getMusic_file_id() {
			return music_file_id;
		}
		@JsonProperty(value="music_file_id", required=true)
		public void setMusic_File_Id(String music_File_Id) {
			this.music_file_id = music_File_Id;
		}
		
		
		public String getMasking_ruler() {
			return masking_ruler;
		}
		@JsonProperty(value="masking_ruler", required=false)
		public void setMasking_ruler(String masking_ruler) {
			this.masking_ruler = masking_ruler;
		}
		
		
		public String getMagnifying_glass() {
			return magnifying_glass;
		}
		@JsonProperty(value="magnifying_glass", required=false)
		public void setMagnifying_glass(String magnifying_glass) {
			this.magnifying_glass = magnifying_glass;
		}
		
		
		public String getExtended_time() {
			return extended_time;
		}
		@JsonProperty(value="extended_time", required=false)
		public void setExtended_time(String extended_time) {
			this.extended_time = extended_time;
		}
		
		
		public String getMasking_tool() {
			return masking_tool;
		}
		@JsonProperty(value="masking_tool", required=false)
		public void setMasking_tool(String masking_tool) {
			this.masking_tool = masking_tool;
		}
		
		public String getMicrophone_headphone() {
			return microphone_headphone;
		}
		@JsonProperty(value="microphone_headphone", required=false)
		public void setMicrophone_headphone(String microphone_headphone) {
			this.microphone_headphone = microphone_headphone;
		}
		
		
		public float getExtended_time_factor() {
			return extended_time_factor;
		}
		@JsonProperty(value="extended_time_factor", required=false)
		public void setExtended_time_factor(float extended_time_factor) {
			this.extended_time_factor = extended_time_factor;
		}
		
	}
	

	/* Getter and Setter Methods */
	public Accomodations getAccomodations() {
		return accomodations;
	}	
	@JsonProperty(value="accomodations", required=true)
	public void setAccomodation(Accomodations accomodations) {
		this.accomodations = accomodations;
	}	

	public Integer getOasStudentId() {
		return _oasStudentId;
	}

	@JsonProperty(value="oasStudentId", required=true)
	public void setOasStudentId(Integer oasStudentId) {
		_oasStudentId = oasStudentId;
	}

	public Integer getOasCustomerId() {
		return _oasCustomerId;
	}

	@JsonProperty(value="oasCustomerId", required=true)
	public void setOasCustomerId(Integer oasCustomerId) {
		_oasCustomerId = oasCustomerId;
	}

	public String getStudentusername() {
		return _studentusername;
	}

	@JsonProperty("studentusername")
	public void setStudentusername(String studentUsername) {
		_studentusername = studentUsername;
	}

	public String getFirstName() {
		return _firstName;
	}

	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		_firstName = firstName;
	}

	public String getMiddleName() {
		return _middleName;
	}

	@JsonProperty("middlename")
	public void setMiddleName(String middleName) {
		_middleName = middleName;
	}

	public String getLastName() {
		return _lastName;
	}

	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		_lastName = lastName;
	}

	public String getBirthdate() {
		return _birthdate;
	}

	@JsonProperty("birthdate")
	public void setBirthdate(String birthdate) {
		_birthdate = birthdate;
	}

	public String getGender() {
		return _gender;
	}

	@JsonProperty("gender")
	public void setGender(String gender) {
		_gender = gender;
	}

	public String getGrade() {
		return _grade;
	}

	@JsonProperty("grade")
	public void setGrade(String grade) {
		_grade = grade;
	}

	public List<HierarchyNode> getHeirarchySet() {
		return _heirarchySet;
	}
	
	@JsonProperty("heirarchySet")
	public void setHeirarchySet(List<HierarchyNode> heirarchySet) {
		_heirarchySet = heirarchySet;
	}

	

	public String getCustomerStudentId() {
		return _customerStudentId;
	}

	@JsonProperty("customerStudentId")
	public void setCustomerStudentId(String oasCustomerStudentId) {
		_customerStudentId = oasCustomerStudentId;
	}

	
	
	
	/*
	public String[] getExtStudentId() {
		return _extStudentId;
	}

	@JsonProperty("studentIds")
	public void setExtStudentId(String[] extStudentId) {
		_extStudentId = extStudentId;
	}
	*/
	
	/*
	 * 
	 * "oasStudentId":1234, "oasCustomerId":1234,
	 * "studentusername":"austin_coffey_1106", "firstName":"John",
	 * "middlename":"A.", "lastName": "Doe", "birthdate":"11/6/1996",
	 * "gender":"M", "grade":"3",
	 * 
	 * "customerStudentId":"788973", "heirarchySet": { "State" :
	 * {OasHeirarchyId":"0", "Code":"0", "Name":"California"}, "District"
	 * :{OasHeirarchyId":"32101", "Code":"98574", "Name":"Monterey"}, "School" :
	 * {OasHeirarchyId":"43185", "Code":"132574", "Name":"Monterey Elementary"},
	 * "Class": [ {"OasHierarchyId":"89089", "Name":"Mrs Jones"},
	 * {"OasHierarchyId":"89065", "Name":"Mrs Smith"} ] }
	 */
}
