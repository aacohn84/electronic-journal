package controllers;

import java.util.Map.Entry;

import play.mvc.Controller;
import play.mvc.Http.Session;

public class BaseController extends Controller {

	public static void printSession() {
		Session s = session();
		for (Entry<String, String> e : s.entrySet()) {
			System.out.println(e.getKey() + ", " + e.getValue());
		}
	}
}
