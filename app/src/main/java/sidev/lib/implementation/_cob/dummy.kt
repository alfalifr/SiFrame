package sidev.lib.implementation._cob

import sidev.lib.android.siframe.tool.util.`fun`.fkmOf
import sidev.lib.implementation.model.Content
import sidev.lib.implementation.model.Page
import sidev.lib.implementation.model.Profile


val profileList= arrayOf(
    Profile("1", "Bejo", "bejo@ok.co", "123abc"),
    Profile("2", "Eko", "eko@ok.co", "abc123"),
    Profile("3", "Tri", "trio@ok.co", "321cba")
)

val contentList= arrayOf(
    Content("1", "Judul 1", "Desc 1"),
    Content("2", "Judul 2", "Desc 2"),
    Content("3", "Judul 3", "Desc 3"),
    Content("4", "Judul 4", "Desc 4"),
    Content("5", "Judul 5", "Desc 5"),
    Content("6", "Judul 6", "Desc 6"),
    Content("7", "Judul 7", "Desc 7"),
    Content("8", "Judul 8", "Desc 8")
)

val pageList= arrayOf(
    Page("1", fkmOf(
        contentList[0].id to contentList[0],
        contentList[1].id to contentList[1],
        contentList[2].id to contentList[2]
    ), 1),
    Page("2", fkmOf(
        contentList[3].id to contentList[3],
        contentList[4].id to contentList[4],
        contentList[5].id to contentList[5]
    ), 2),
    Page("3", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 3),
    Page("4", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 4),
    Page("5", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 5),
    Page("6", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 6),
    Page("7", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 7),
    Page("8", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 8),
    Page("9", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 9),
    Page("10", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 10),
    Page("11", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 11),
    Page("12", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 12),
    Page("13", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 13),
    Page("14", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 14),
    Page("15", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 15),
    Page("16", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 16),
    Page("17", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 17),
    Page("18", fkmOf(
        contentList[5].id to contentList[5],
        contentList[7].id to contentList[7]
    ), 18)
)