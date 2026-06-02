package ci.nsu.mobile.main.data.models

import com.google.gson.annotations.SerializedName

//data class - специальный класс Kotlin для хранения данных (автоматически генерирует equals(), hashCode(), toString())
data class GroupDto(
    @SerializedName("groupId")
    val id: Int,
    @SerializedName("groupName")
    val name: String
)
//@SerializedName - говорит Gson: "поле на сервере называется иначе, чем в коде"
//Сервер присылает: {"groupId":1,"groupName":"2201а1"}
//Мы получаем: GroupDto(id=1, name="2201а1")