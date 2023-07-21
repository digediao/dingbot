package test;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.Context;
import com.azure.search.documents.SearchClient;
import com.azure.search.documents.SearchClientBuilder;
import com.azure.search.documents.indexes.SearchIndexClient;
import com.azure.search.documents.indexes.SearchIndexClientBuilder;
import com.azure.search.documents.indexes.models.IndexDocumentsBatch;
import com.azure.search.documents.indexes.models.SearchIndex;
import com.azure.search.documents.indexes.models.SearchSuggester;
import com.azure.search.documents.models.SearchOptions;
import com.azure.search.documents.util.AutocompletePagedIterable;
import com.azure.search.documents.util.SearchPagedIterable;
import dingding.bot.AzureOpenaiDingdingBotApplication;
import dingding.bot.pojo.Address;
import dingding.bot.pojo.Hotel;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;

import static dingding.bot.util.constant.azureSearchConstant.*;

@SpringBootTest(classes = AzureOpenaiDingdingBotApplication.class)
public class test {
    @Test
    public void test(){
        //连接认知搜索客户端
        var searchServiceEndpoint = SEARCH_SERVICE_URL;
        var adminKey = new AzureKeyCredential(SEARCH_SERVICE_ADMIN_KEY);
        String indexName = SEARCH_INDEX_NAME;
        //建立索引的增、删、改操作客户端
        SearchIndexClient searchIndexClient = new SearchIndexClientBuilder()
                .endpoint(searchServiceEndpoint)
                .credential(adminKey)
                .buildClient();
        //调用 createOrUpdateIndex 方法以在搜索服务中创建索引。 此索引还包括一个 SearchSuggester 以便在指定字段上启用自动完成。
        searchIndexClient.createOrUpdateIndex(
                new SearchIndex(indexName, SearchIndexClient.buildSearchFields(Hotel.class, null))
                        .setSuggesters(new SearchSuggester("sg", Arrays.asList("HotelName"))));

        //建立搜索查询操作
        SearchClient searchClient = new SearchClientBuilder()
                .endpoint(searchServiceEndpoint)
                .credential(adminKey)
                .indexName(indexName)
                .buildClient();


        // 将文档上传到搜索索引
        uploadDocuments(searchClient);

        //等待2秒钟以完成索引，然后再启动查询
        System.out.println("Waiting for indexing...\n");
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {}


        //执行搜索文档
        System.out.println("Starting queries...\n");
        RunQueries(searchClient);
        System.out.println("Complete.\n");
    }


    // Upload documents in a single Upload request.加载文档
    private static void uploadDocuments(SearchClient searchClient) {
        var hotelList = new ArrayList<Hotel>();

        var hotel = new Hotel();
        hotel.hotelId = "1";
        hotel.hotelName = "Secret Point Motel";
        hotel.description = "The hotel is ideally located on the main commercial artery of the city in the heart of Beijing. A few minutes away is Time's Square and the historic centre of the city, as well as other places of interest that make Beijing one of America's most attractive and cosmopolitan cities.";
        hotel.descriptionFr = "L'hôtel est idéalement situé sur la principale artère commerciale de la ville en plein cœur de Beijing. A quelques minutes se trouve la place du temps et le centre historique de la ville, ainsi que d'autres lieux d'intérêt qui font de Beijing l'une des villes les plus attractives et cosmopolites de l'Amérique.";
        hotel.category = "Boutique";
        hotel.tags = new String[] { "pool", "air conditioning", "concierge" };
        hotel.parkingIncluded = false;
        hotel.lastRenovationDate = OffsetDateTime.of(LocalDateTime.of(LocalDate.of(1970, 1, 18),
                LocalTime.of(0, 0)), ZoneOffset.UTC);
        hotel.rating = 3.6;
        hotel.address = new Address();
        hotel.address.streetAddress = "677 5th Ave";
        hotel.address.city = "Beijing";
        hotel.address.stateProvince = "NY";
        hotel.address.postalCode = "10022";
        hotel.address.country = "USA";
        hotelList.add(hotel);

        hotel = new Hotel();
        hotel.hotelId = "2";
        hotel.hotelName = "Twin Dome Motel";
        hotel.description = "The hotel is situated in a  nineteenth century plaza, which has been expanded and renovated to the highest architectural standards to create a modern, functional and first-class hotel in which art and unique historical elements coexist with the most modern comforts.";
        hotel.descriptionFr = "L'hôtel est situé dans une place du XIXe siècle, qui a été agrandie et rénovée aux plus hautes normes architecturales pour créer un hôtel moderne, fonctionnel et de première classe dans lequel l'art et les éléments historiques uniques coexistent avec le confort le plus moderne.";
        hotel.category = "Boutique";
        hotel.tags = new String[] { "pool", "free wifi", "concierge" };
        hotel.parkingIncluded = false;
        hotel.lastRenovationDate = OffsetDateTime.of(LocalDateTime.of(LocalDate.of(1979, 2, 18),
                LocalTime.of(0, 0)), ZoneOffset.UTC);
        hotel.rating = 3.60;
        hotel.address = new Address();
        hotel.address.streetAddress = "140 University Town Center Dr";
        hotel.address.city = "Sarasota";
        hotel.address.stateProvince = "FL";
        hotel.address.postalCode = "34243";
        hotel.address.country = "USA";
        hotelList.add(hotel);

        hotel = new Hotel();
        hotel.hotelId = "3";
        hotel.hotelName = "Triple Landscape Hotel";
        hotel.description = "The Hotel stands out for its gastronomic excellence under the management of William Dough, who advises on and oversees all of the Hotel's restaurant services.";
        hotel.descriptionFr = "L'hôtel est situé dans une place du XIXe siècle, qui a été agrandie et rénovée aux plus hautes normes architecturales pour créer un hôtel moderne, fonctionnel et de première classe dans lequel l'art et les éléments historiques uniques coexistent avec le confort le plus moderne.";
        hotel.category = "Resort and Spa";
        hotel.tags = new String[] { "air conditioning", "bar", "continental breakfast" };
        hotel.parkingIncluded = true;
        hotel.lastRenovationDate = OffsetDateTime.of(LocalDateTime.of(LocalDate.of(2015, 9, 20),
                LocalTime.of(0, 0)), ZoneOffset.UTC);
        hotel.rating = 4.80;
        hotel.address = new Address();
        hotel.address.streetAddress = "3393 Peachtree Rd";
        hotel.address.city = "Atlanta";
        hotel.address.stateProvince = "GA";
        hotel.address.postalCode = "30326";
        hotel.address.country = "USA";
        hotelList.add(hotel);

        hotel = new Hotel();
        hotel.hotelId = "4";
        hotel.hotelName = "Sublime Cliff Hotel";
        hotel.description = "Sublime Cliff Hotel is located in the heart of the historic center of Sublime in an extremely vibrant and lively area within short walking distance to the sites and landmarks of the city and is surrounded by the extraordinary beauty of churches, buildings, shops and monuments. Sublime Cliff is part of a lovingly restored 1800 palace.";
        hotel.descriptionFr = "Le sublime Cliff Hotel est situé au coeur du centre historique de sublime dans un quartier extrêmement animé et vivant, à courte distance de marche des sites et monuments de la ville et est entouré par l'extraordinaire beauté des églises, des bâtiments, des commerces et Monuments. Sublime Cliff fait partie d'un Palace 1800 restauré avec amour.";
        hotel.category = "Boutique";
        hotel.tags = new String[] { "concierge", "view", "24-hour front desk service" };
        hotel.parkingIncluded = true;
        hotel.lastRenovationDate = OffsetDateTime.of(LocalDateTime.of(LocalDate.of(1960, 2, 06),
                LocalTime.of(0, 0)), ZoneOffset.UTC);
        hotel.rating = 4.60;
        hotel.address = new Address();
        hotel.address.streetAddress = "7400 San Pedro Ave";
        hotel.address.city = "San Antonio";
        hotel.address.stateProvince = "TX";
        hotel.address.postalCode = "78216";
        hotel.address.country = "USA";
        hotelList.add(hotel);

        var batch = new IndexDocumentsBatch<Hotel>();
        batch.addMergeOrUploadActions(hotelList);
        try {
            searchClient.indexDocuments(batch);
        }
        catch (Exception e) {
            e.printStackTrace();
            // If for some reason any documents are dropped during indexing, you can compensate by delaying and
            // retrying. This simple demo just logs failure and continues
            System.err.println("Failed to index some of the documents");}
    }

    /**
     *搜索索引
     */
    // 将搜索结果输出到控制台
    private static void WriteSearchResults(SearchPagedIterable searchResults) {
        searchResults.iterator().forEachRemaining(result -> {
            Hotel hotel = result.getDocument(Hotel.class);
            System.out.println(hotel);
        });

        System.out.println();
    }
    private static void WriteAutocompleteResults(AutocompletePagedIterable autocompleteResults) {
        autocompleteResults.iterator().forEachRemaining(result -> {
            String text = result.getText();
            System.out.println(text);
        });

        System.out.println();
    }
    //创建 RunQueries 方法用于执行查询并返回结果。 结果是 Hotel 对象。 此示例显示了方法签名和第一个查询。 此查询演示了 Select 参数，通过该参数可以使用文档中的选定字段来编写结果
    private static void RunQueries(SearchClient searchClient) {
        // Query 1
        System.out.println("Query #1: 搜索空术语“*”以返回所有文档，显示字段的子集\n");

        SearchOptions options = new SearchOptions();
        options.setIncludeTotalCount(true);
        options.setFilter("");
        options.setOrderBy("");
        options.setSelect("HotelId", "HotelName", "Address/City");

        WriteSearchResults(searchClient.search("*", options, Context.NONE));


        //在第二个查询中，搜索某个术语，添加筛选器（用于选择评级大于 4 的文档），然后按评级降序排序。 筛选器是布尔表达式，该表达式通过索引中的 isFilterable 字段求值。
        // 筛选器查询包括或排除值。 同样，筛选器查询没有关联的相关性分数。
        // Query 2
        System.out.println("Query #2: 搜索“hotel”，筛选“rating gt 4”，按评分降序排序...\n");

        options = new SearchOptions();
        options.setFilter("Rating gt 4");
        options.setOrderBy("Rating desc");
        options.setSelect("HotelId", "HotelName", "Rating");

        WriteSearchResults(searchClient.search("hotels", options, Context.NONE));


        //第三个查询演示了用于将全文搜索操作的范围限定为特定字段的 searchFields。
        // Query 3
        System.out.println("Query #3: 将搜索限制为特定字段（Tags）...\n");

        options = new SearchOptions();
        options.setSearchFields("Tags");

        options.setSelect("HotelId", "HotelName", "Tags");

        WriteSearchResults(searchClient.search("pool", options, Context.NONE));


        //第四个查询演示了 Facet，可用于构建分面导航结构。
        // Query 4
        System.out.println("Query #4: “类别”上的分面...\n");

        options = new SearchOptions();
        options.setFilter("");
        options.setFacets("Category");
        options.setSelect("HotelId", "HotelName", "Category");

        WriteSearchResults(searchClient.search("*", options, Context.NONE));


        //在第五个查询中，返回一个特定文档。
        // Query 5
        System.out.println("Query #5: 查找特定文档...\n");

        Hotel lookupResponse = searchClient.getDocument("3", Hotel.class);
        System.out.println(lookupResponse.hotelId);
        System.out.println();


        //最后一个查询显示了“自动完成”的语法，它模拟部分用户输入，即“s”，该输入解析为 sourceFields 中的两个可能的匹配项，与你在索引中定义的建议器相关联。
        // Query 6
        System.out.println("Query #6: 在以“s”开头的HotelName上调用自动完成...\n");

        WriteAutocompleteResults(searchClient.autocomplete("s", "sg"));
    }

}
