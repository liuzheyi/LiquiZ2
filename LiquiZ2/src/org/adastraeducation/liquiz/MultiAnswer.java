package org.adastraeducation.liquiz;

import java.util.ArrayList;
import java.util.Arrays;

import org.adastraeducation.liquiz.util.Util;


public class MultiAnswer extends MultiChoiceDropdown {
		
	/**********************Added getter and setter for serialization********************************/
//	TODO: change serialization to use getAnsAsArray() and setAns() in Question class

	public MultiAnswer() {
	}
	
	public MultiAnswer(int id, int points, int level, ArrayList<Answer> answers) {
		super(id, points, level, answers);
	}
	public MultiAnswer(int points, int level, ArrayList<Answer> answers) {
		super(points, level, answers);
	}
	
	public MultiAnswer(int points, int level, String stdChoiceName, int [] rightAns) {
		super(points, level, stdChoiceName);
		for (int i : rightAns) {
			this.getAns().get(i).setCorrect(true);
		}
	}
	
	public MultiAnswer(int id, int points, int level) {
		super (id, points, level);
	}

	public double grade(String[] s) {
		double pointsPerAns = ((double) getPoints())/getAns().size();
		double points = 0;
		boolean answerFound = false;
		
//		for (String res : s) {
//			System.out.println(res);
//		}
		
		for (Answer ans : getAns()) {
			answerFound = false;
			for (String res : s) { // look for answer choice in student responses
				if (ans.getName().equals(res)) { // Student provided this answer
					if(ans.getCorrect()) { // Student provided an answer that was correct
						points += pointsPerAns;
					}
					answerFound = true;
					break; // Look for next Answer choice
				}
			}
			if(!answerFound && !ans.getCorrect()) { // Student did not provide this answer and this answer was false
				points += pointsPerAns;
			}
		}
		return points;
	}
	
	public static void main(String[] args) {
		// Testing the grade method
		
		MultiAnswer q = new MultiAnswer(1, 2, 1, new ArrayList<Answer>(Arrays.asList(
			new Answer(new Text("Choose this"), true),
			new Answer(new Text("Not this"), false)
		)));
		String[] s1 = {"Choose this"};
		System.out.println(q.grade(s1) + " should equal 2");
		String[] s2 = {};
		System.out.println(q.grade(s2) + " should equal 1");
		String[] s3 = {"Not this"};
		System.out.println(q.grade(s3) + " should equal 0");
		String[] s4 = {"Choose this","Not this"};
		System.out.println(q.grade(s4) + " should equal 1");
	}
	
	public void writeHTML(StringBuilder b ){
		
		b.append("<select name='").append(getId()).append("' form='quizForm' multiple>\n");
		for (Answer ans : getAns()){
			b.append("<option value= '").append(ans.getName()).append("'>");
			ans.writeHTML(b);
			b.append("  </option>\n ");
		 }
		b.append("</select>\n");
       
	}
	public void writeJS(StringBuilder b ){
		Util.writeAnsListAsJS("multiAnswer", getAns(), b);
	}	
}
