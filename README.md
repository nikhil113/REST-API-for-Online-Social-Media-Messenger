# REST-API-for-Online-Social-Media-Messenger

Overall Functionality:

1. User will first sign up by passing all relevant informations.During sign up user will be assigned unique id and that id will be returned in the response.

2. In order to access any end point or API, user should first get access token. For getting access token, user should pass id and password. The server will first verify id and password.If it get validated,
  then server will send one token which the user can use to access other API. The token will actually comprise of "ID:MD5 hash of password ,then space and then time in milliseconds" till it is valid.
  Example- USER_1:hashvalue 12330049948.

3. Whenever user has to access some end point or API, he will pass token as authorization header. First the token will be validated by the filter and if validated, then he will be redirected to the 
   corresponding API.
   
4. UserProfile- A person can do sign up using a POST API. A user can also get Profile Informations of other users by GET API. But he can get all fields if he is a friend of that person or he will get only 
	those fields which are marked as PUBLIC by other users.A user can also update his profile by using PUT API.A user can also delete profile by using DELETE API.

5. Message Linked With Profile- A user can get all the messages belonging to a profile but he can get all messages if he is a friend of that person or only those messages that are markes as PUBLIC by other 
	users.Then a user can also get specific message by passing message Id. A person can also post,update and delete messages.

6. Comment- A user can post comments on any message.A user can get all comments belonging to a message.Now here, a user can get only those comments which are marked as PUBLIC or the owner of the comment is a 
	friend of that particular user.A user can also update or delete his own comments.

7. Like- A person can post like for a message.A person can get all like information of a message.A person can also delete his like for a particular message.

8. Share -This case is similar to like resource.

9. Permission- A person can also update all his permission settings.

10.Friend Request- A person can send friend request to any person and can also confirm the friend request posted to him.