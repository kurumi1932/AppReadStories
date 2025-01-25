package huce.fit.appreadstories.shared_preferences

import android.content.Context

class AccountSharedPreferences(context: Context) : BaseSharedPreferences(context) {

    fun getAccountId(): Int {
        return sharedPreferences.getInt("accountId", 0)
    }

    fun setAccountId(accountId: Int) {
        edit.putInt("accountId", accountId)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString("username", "")
    }

    fun setUsername(username: String) {
        edit.putString("username", username)
    }

    fun getEmail(): String? {
        return sharedPreferences.getString("email", "")
    }

    fun setEmail(email: String) {
        edit.putString("email", email)
    }

    fun getName(): String? {
        return sharedPreferences.getString("name", "")
    }

    fun setName(name: String) {
        edit.putString("name", name)
    }

    fun getBirthday(): String? {
        return sharedPreferences.getString("birthday", "")
    }

    fun setBirthday(birthday: String) {
        edit.putString("birthday", birthday)
    }

    fun getAge(): Int {
        return sharedPreferences.getInt("age", 0)
    }

    fun setAge(age: Int) {
        edit.putInt("age", age)
    }
}
