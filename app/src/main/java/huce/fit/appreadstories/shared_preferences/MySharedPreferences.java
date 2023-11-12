package huce.fit.appreadstories.shared_preferences;

public interface MySharedPreferences {

     void getMySharedPreferences(String file, int modePrivate);

     void setMySharedPreferences(String file, int modePrivate);

     void myApply();

     void myRemove();

     int getIdAccount();

     void setIdAccount(int idAccount);

     String getUsername();

     void setUsername(String username);

     String getEmail();

     void setEmail(String email);

     String getName();

     void setName(String name);

     String getBirthday();

     void setBirthday(String birthday);

     int getAge();

     void setAge(int age);
}
