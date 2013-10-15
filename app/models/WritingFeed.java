package models;

import java.util.Map;

public class WritingFeed {
	private Map<Prompt, Response> responses;

	public Map<Prompt, Response> getResponses() { return responses; }

	public void setResponses(Map<Prompt, Response> responses) { this.responses = responses; }
}
