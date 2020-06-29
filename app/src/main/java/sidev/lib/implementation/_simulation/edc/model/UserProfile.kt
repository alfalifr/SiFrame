package id.go.surabaya.ediscont.models

import java.io.Serializable


data class UserProfile(val _id: String?,
                  var ownerName: String?,
                  var businessName: String?,
                  var address: String?,
                  var contact: String?,
                  var workingHour: String?,
                  var subDistrict: String?,
                  var village: String?,
                  var urlPict: String?,
                  var lat: String?,
                  var long: String?,
                  var type: Int?= null, // -> hanya untuk list di OPD dkk
                  var idKec: Int?= null // untuk filter pada saat  di kulakan list
                    ) : Serializable
