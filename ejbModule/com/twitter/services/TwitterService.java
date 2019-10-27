package com.twitter.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.twitter.Interfaces.ITwitter;
import com.twitter.dto.TwitterDTO;
import com.twitter.entities.TwitterDetail;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

@Stateless
@LocalBean
public class TwitterService implements ITwitter {

	@PersistenceContext
	EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<TwitterDTO> getAllTweetsByID() {
		Query query = em.createNamedQuery("TwitterDetail.findAll");
		return (List<TwitterDTO>) query.getResultList();

	}

	@Override
	public TwitterDetail saveTweet(TwitterDTO twitterDTO) {
		TwitterDetail twitterDetails = new TwitterDetail();
		if (twitterDTO != null) {
			twitterDetails.setTweetBody(twitterDTO.getTweetBody());
			twitterDetails.setTimeStamp(new Date());
			em.persist(twitterDetails);
			postTweet(twitterDetails.getTweetBody());
		}
		return twitterDetails;
	}

	@Override
	public void postTweet(String tweetBody) {
		String consumerKey = "W1ALc4diEjl1kdj3Is2Rp98MX";

		// Your Twitter App's Consumer Secret
		String consumerSecret = "HdGpIxPf4u4mHHBh8jIiXHx5qeqNsXflrgavbKkVK844TqDEuf";

		// Your Twitter Access Token
		String accessToken = "1487371147-qgLyLbnhjaXnSEcRFdC1jKPvTAirXvIu6NMxbh2k ";

		// Your Twitter Access Token Secret
		String accessTokenSecret = "ujYaVtkw2ubTMHfdnIuuJ9gENvIGgS5bq6XVB5fVS8zvX";
		
		System.out.println("TRYING TO POST THIS TWEET " + tweetBody);
		// Instantiate a re-usable and thread-safe factory
		TwitterFactory twitterFactory = new TwitterFactory();

		// Instantiate a new Twitter instance
		Twitter twitter = twitterFactory.getInstance();

		// setup OAuth Consumer Credentials
		twitter.setOAuthConsumer(consumerKey, consumerSecret);

		// setup OAuth Access Token
		twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

		// Instantiate and initialize a new twitter status update
		StatusUpdate statusUpdate = new StatusUpdate(tweetBody);
		// attach any media, if you want to
		/*
		 * statusUpdate.setMedia( //title of media "http://simpledeveloper.com" , new
		 * URL("https://si0.twimg.com/profile_images/1733613899/Published_Copy_Book.jpg"
		 * ).openStream());
		 */

		// tweet or update status
		Status status = null;
		try {
			status = twitter.updateStatus(statusUpdate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// response from twitter server
		if (status != null) {
			System.out.println("status.toString() = " + status.toString());
			System.out.println("status.getInReplyToScreenName() = " + status.getInReplyToScreenName());
			System.out.println("status.getSource() = " + status.getSource());
			System.out.println("status.getText() = " + status.getText());

			System.out.println("status.getURLEntities() = " + Arrays.toString(status.getURLEntities()));
			System.out.println("status.getUserMentionEntities() = " + Arrays.toString(status.getUserMentionEntities()));
		}

	}

}
