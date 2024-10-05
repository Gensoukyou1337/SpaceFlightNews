package com.ivan.data

import com.ivan.data.models.Item
import com.ivan.data.models.ItemsListPage
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class DeserializationUnitTest {

    private val reportsRawJson =
        """
            {
              "count": 1323,
              "next": "https://api.spaceflightnewsapi.net/v4/blogs/?limit=10&offset=10",
              "previous": null,
              "results": [
                {
                  "id": 1381,
                  "title": "Glitter and glow",
                  "url": "https://www.planetary.org/the-downlink/glitter-and-glow",
                  "image_url": "https://planetary.s3.amazonaws.com/web/assets/email/newsletter/_1200x799_crop_center-center_82_line/aurora-iss-71.jpg",
                  "news_site": "Planetary Society",
                  "summary": "This week we look forward to launches, gaze at glowing auroras, and get creative with glitter.",
                  "published_at": "2024-10-04T14:37:31Z",
                  "updated_at": "2024-10-04T14:37:31.445736Z",
                  "featured": false,
                  "launches": [],
                  "events": []
                },
                {
                  "id": 1380,
                  "title": "Europa Clipper: A mission backed by advocates",
                  "url": "https://www.planetary.org/articles/europa-clipper-a-mission-backed-by-advocates",
                  "image_url": "https://planetary.s3.amazonaws.com/web/assets/pictures/_2400x3596_crop_center-center_82_line/20150515_BN_Full_072_ca.jpg",
                  "news_site": "Planetary Society",
                  "summary": "Europa Clipper will soon head for Jupiter's icy, potentially habitable moon. Without the advocacy efforts of The Planetary Society and our members, the mission may never have been possible.",
                  "published_at": "2024-10-01T14:07:37Z",
                  "updated_at": "2024-10-01T14:07:37.463726Z",
                  "featured": false,
                  "launches": [],
                  "events": []
                },
                {
                  "id": 1379,
                  "title": "Cert-2: Dress rehearsal of Vulcan launch day planned",
                  "url": "https://blog.ulalaunch.com/blog/cert-2-dress-rehearsal-of-vulcan-launch-day-planned",
                  "image_url": "https://blog.ulalaunch.com/hubfs/v_cert2_r9a.jpg",
                  "news_site": "United Launch Alliance",
                  "summary": "",
                  "published_at": "2024-10-01T12:37:37Z",
                  "updated_at": "2024-10-01T12:37:37.949114Z",
                  "featured": false,
                  "launches": [],
                  "events": []
                },
                {
                  "id": 1378,
                  "title": "Weekly Roundup for SpacePolicyOnline.com: September 16-29, 2024",
                  "url": "https://spacepolicyonline.com/news/weekly-roundup-for-spacepolicyonline-com-september-16-29-2024/",
                  "image_url": "https://pbs.twimg.com/profile_images/548027983/Website_logo_400x400.jpg",
                  "news_site": "SpacePolicyOnline.com",
                  "summary": "Here are links to all the articles published on SpacePolicyOnline.com from September 16-29, 2024 including our “What’s Happening in Space Policy” for this coming week. Click on each title to...",
                  "published_at": "2024-09-30T12:34:31Z",
                  "updated_at": "2024-09-30T12:37:43.715070Z",
                  "featured": false,
                  "launches": [],
                  "events": []
                },
                {
                  "id": 1377,
                  "title": "Cloudy skies, smooth sailing",
                  "url": "https://www.planetary.org/the-downlink/cloudy-skies-smooth-sailing",
                  "image_url": "https://planetary.s3.amazonaws.com/web/assets/email/newsletter/_768x240_crop_center-center_82_line/cloud-streets-vastitas.jpg",
                  "news_site": "Planetary Society",
                  "summary": "A Martian cloud atlas, LightSail wins big, and multiple missions coast toward launch.",
                  "published_at": "2024-09-27T14:37:55Z",
                  "updated_at": "2024-09-27T14:37:55.035045Z",
                  "featured": false,
                  "launches": [],
                  "events": []
                },
                {
                  "id": 1376,
                  "title": "How to spot Comet Tsuchinshan-Atlas",
                  "url": "https://www.planetary.org/articles/how-to-spot-comet-tsuchinshan-atlas",
                  "image_url": "https://planetary.s3.amazonaws.com/web/assets/pictures/_2400x3596_crop_center-center_82_line/20150515_BN_Full_072_ca.jpg",
                  "news_site": "Planetary Society",
                  "summary": "Catch this comet over the next few days, and check back in a couple weeks when it may shine even brighter.",
                  "published_at": "2024-09-26T16:57:31Z",
                  "updated_at": "2024-09-26T16:57:31.371403Z",
                  "featured": false,
                  "launches": [],
                  "events": []
                },
                {
                  "id": 1375,
                  "title": "The Hera launch: What to expect",
                  "url": "https://www.planetary.org/articles/hera-launch-what-to-expect",
                  "image_url": "https://planetary.s3.amazonaws.com/web/assets/pictures/_1200x675_crop_center-center_82_line/653473/dimorphos_northup_HDTV.jpg",
                  "news_site": "Planetary Society",
                  "summary": "The European Space Agency (ESA) is preparing to launch a mission to study the aftermath of DART's impact on the asteroid moonlet Dimorphos.",
                  "published_at": "2024-09-26T14:07:32Z",
                  "updated_at": "2024-09-26T14:07:32.529166Z",
                  "featured": false,
                  "launches": [],
                  "events": []
                },
                {
                  "id": 1374,
                  "title": "Could Europa Clipper find life?",
                  "url": "https://www.planetary.org/articles/could-europa-clipper-find-life",
                  "image_url": "https://planetary.s3.amazonaws.com/web/assets/pictures/_768x768_crop_center-center_82_line/jupiter-europa-and-clipper.jpg",
                  "news_site": "Planetary Society",
                  "summary": "For a mission that doesn’t aim to find alien life, Europa Clipper may come surprisingly close.",
                  "published_at": "2024-09-25T14:07:57Z",
                  "updated_at": "2024-09-25T14:07:57.326850Z",
                  "featured": false,
                  "launches": [],
                  "events": []
                },
                {
                  "id": 1373,
                  "title": "Cert-2: Vulcan rocket readied for second flight test",
                  "url": "https://blog.ulalaunch.com/blog/cert-2-vulcan-rocket-readied-for-second-flight-test",
                  "image_url": "https://blog.ulalaunch.com/hubfs/cert2_fullstack.jpg",
                  "news_site": "United Launch Alliance",
                  "summary": "",
                  "published_at": "2024-09-21T17:36:14Z",
                  "updated_at": "2024-09-21T17:36:14.430284Z",
                  "featured": false,
                  "launches": [],
                  "events": []
                },
                {
                  "id": 1372,
                  "title": "Where Congress Stands on NASA's 2025 budget",
                  "url": "https://www.planetary.org/articles/where-we-stand-on-nasas-2025-budget-fall-2024",
                  "image_url": "https://planetary.s3.amazonaws.com/web/assets/pictures/_2400x3596_crop_center-center_82_line/20150515_BN_Full_072_ca.jpg",
                  "news_site": "Planetary Society",
                  "summary": "Weeks before the new fiscal year, Congress still hasn't finalized NASA's 2025 budget.",
                  "published_at": "2024-09-20T14:36:09Z",
                  "updated_at": "2024-09-20T14:36:09.319041Z",
                  "featured": false,
                  "launches": [],
                  "events": []
                }
              ]
            }
        """.trimIndent()

    private val rawArticleJson = """
        {"id":26462,"title":"JOB OPPORTUNITIES: NASA OFFICE OF THE GENERAL COUNSEL","url":"https://www.nasa.gov/general/job-opportunities-nasa-office-of-the-general-counsel/","image_url":"https://www.nasa.gov/wp-content/uploads/2024/10/american-flag-on-station.jpg?w=300","news_site":"NASA","summary":"There are no current openings. Please check back later for opportunities to join our team.","published_at":"2024-10-04T21:07:56Z","updated_at":"2024-10-04T21:17:35.436046Z","featured":false,"launches":[],"events":[]}
    """.trimIndent()

    @Test
    fun testDeserializeAsItemsListPage() {
        val deserialized = Json.decodeFromString<ItemsListPage>(reportsRawJson)
        assert(deserialized.results.size == 10)
    }

    @Test
    fun testDeserializeSingleItem() {
        val deserialized = Json.decodeFromString<Item>(rawArticleJson)
        assert(deserialized.id == 26462)
    }

}