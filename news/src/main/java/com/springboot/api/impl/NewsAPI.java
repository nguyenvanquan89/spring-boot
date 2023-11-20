package com.springboot.api.impl;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.api.INewsAPI;
import com.springboot.dto.NewsDTO;
import com.springboot.dto.Views;
import com.springboot.entity.NewsEntity;
import com.springboot.exceptions.ResourceNotFoundException;
import com.springboot.service.INewsService;
import com.springboot.util.Constants;
import com.springboot.util.LocaleUtils;
import com.springboot.util.MappingUtils;
import com.springboot.util.MessageKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/news")
public class NewsAPI extends BaseAPI<NewsDTO, NewsEntity> implements INewsAPI {

  @Autowired
  private MappingUtils mappingUtils;

  @Autowired
  private LocaleUtils localeUtils;

  @Autowired
  private INewsService newsService;

  @Autowired
  ObjectMapper objectMapper;

  @Value("${file.upload.directory}")
  private String directoryUpload;

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @JsonView(Views.AddNewView.class)
  @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
  public ResponseEntity<Map<String, Object>> create(@Valid @RequestPart("data") String strDto,
      NewsEntity entity,
      @RequestPart(value = "file", required = false) MultipartFile file)
      throws IOException {

    NewsDTO dto = objectMapper.readValue(strDto, NewsDTO.class);
    dto.setThumbnail(storeFile(file, dto.getThumbnail()));
    mappingUtils.mapFromDTO(dto, entity);
    entity = newsService.save(entity);
    mappingUtils.map(entity, dto);
    String msg = localeUtils.getMessageByKey(MessageKeys.ADD_NEW_SUCCESS, null);
    Map<String, Object> body = new HashMap<>();
    body.put("data", dto);
    body.put("message", msg);
    return new ResponseEntity<>(body, HttpStatus.CREATED);
  }

  @Override
  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @JsonView(Views.UpdateView.class)
  @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
  public ResponseEntity<Map<String, Object>> update(@Valid @RequestPart("data") String strDto,
      NewsEntity entity,
      @RequestPart(value = "file", required = false) MultipartFile file)
      throws IOException {
    NewsDTO dto = objectMapper.readValue(strDto, NewsDTO.class);
    dto.setThumbnail(storeFile(file, dto.getThumbnail()));
    mappingUtils.mapFromDTO(dto, entity);
    entity = newsService.save(entity);
    mappingUtils.map(entity, dto);
    String msg = localeUtils.getMessageByKey(MessageKeys.UPDATE_SUCCESS, null);
    Map<String, Object> body = new HashMap<>();
    body.put("data", dto);
    body.put("message", msg);
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

    /*@PostMapping(value = "/images/upload")
    @JsonView({Views.UpdateView.class, Views.AddNewView.class})
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
    public ResponseEntity<?> updateNewsUploadImage(@Valid @RequestBody NewsDTO dto,
                                                   MultipartHttpServletRequest request,
                                                   NewsEntity entity) throws IOException {
        NewsDTO dto = new NewsDTO();
        Iterator<String> iterator = request.getFileNames();
        MultipartFile file = request.getFile(iterator.next());
        dto.setThumbnail(storeFile(file, dto.getThumbnail()));
        mappingUtils.mapFromDTO(dto, entity);
        entity = newsService.save(entity);
        mappingUtils.map(entity, dto);
        String msg = localeUtils.getMessageByKey(MessageKeys.UPDATE_SUCCESS, null);
        Map<String, Object> body = new HashMap<>();
        body.put("data", dto);
        body.put("message", msg);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }*/

  @Override
  @GetMapping("/s")
  @JsonView(Views.SearchView.class)
  public ResponseEntity<Map<String, Object>> searchNews(
      @RequestParam(defaultValue = "12") int itemPerPage,
      @RequestParam(defaultValue = "1") int currentPage,
      @RequestParam(defaultValue = "0") long categoryId,
      @RequestParam(required = false, defaultValue = "DESC") String order,
      @RequestParam(required = false, defaultValue = "modifiedDate") String orderColumn,
      @RequestParam(defaultValue = "") String keyword) {

    Sort sort = new Sort(Sort.Direction.ASC, orderColumn);
    if (Constants.DESC_REQUEST.equalsIgnoreCase(order)) {
      sort = new Sort(Sort.Direction.DESC, orderColumn);
    }
    // find all in database
    Pageable pageable = new PageRequest(currentPage - 1, itemPerPage, sort);
    Page<NewsEntity> pageEntity = newsService.searchNews(categoryId, keyword, pageable);
    List<NewsDTO> lstDto = mappingUtils.mapList(pageEntity.getContent(), NewsDTO.class);

    Map<String, Object> body = new HashMap<>();
    body.put(Constants.TOTAL_RECORD_RESPONSE, pageEntity.getTotalElements());
    body.put(Constants.TOTAL_PAGE_RESPONSE, pageEntity.getTotalPages());
    body.put(Constants.ITEM_PER_PAGE_RESPONSE, itemPerPage);
    body.put(Constants.CURRENT_PAGE_RESPONSE, currentPage);
    body.put(Constants.LST_RESULT_RESPONSE, lstDto);

    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  private String storeFile(MultipartFile file, String oldFileName) throws IOException {
    String uniqueFilename = "";
    if (file != null) {
      //check file size
      if (file.getSize() > 5 * 1024 * 1024) {
        throw new IllegalArgumentException(
            localeUtils.getMessageByKey(
                MessageKeys.FILE_TOO_LARGE_ERROR,
                null));
      }
      String contentType = file.getContentType();
      if (contentType == null || !contentType.startsWith("image/")) {
        throw new IllegalArgumentException(localeUtils.getMessageByKey(
            MessageKeys.FILE_UNSUPPORTED_MEDIA_TYPE,
            null));
      }

      String filename = StringUtils.cleanPath(file.getOriginalFilename());
      uniqueFilename = UUID.randomUUID() + "_" + filename;
      Path uploadDir = Paths.get(directoryUpload);
      //check directory upload is exist
      if (!Files.exists(uploadDir)) {
        Files.createDirectories(uploadDir);
      }
      //get full path to save file
      Path destination = Paths.get(uploadDir.toString(), uniqueFilename);

      //get full path of old file and delete
      if (oldFileName != null && !StringUtils.isEmpty(oldFileName)) {
        Path oldFile = Paths.get(uploadDir.toString(), oldFileName);
        Files.delete(oldFile);
      }
      //Save upload file to directory
      Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

    }
    return uniqueFilename;
  }


  @Override
  @GetMapping("/{id}")
  @JsonView({Views.SearchView.class})
  public ResponseEntity<NewsDTO> findById(@PathVariable("id") Long id,
      NewsDTO dto)
      throws ResourceNotFoundException {
    return super.findById(id, dto);
  }

  /**
   * @param imageName full name of image
   * @return image need to view
   * @throws MalformedURLException     if error view image
   * @throws ResourceNotFoundException if not found image
   */
  @GetMapping("/images/{imageName:.+}")
  public ResponseEntity<UrlResource> viewImage(@PathVariable String imageName)
      throws MalformedURLException, ResourceNotFoundException {

    Path imagePath = Paths.get(directoryUpload, imageName);
    UrlResource urlResource = new UrlResource(imagePath.toUri());
    if (!urlResource.exists()) {
      Object[] obj = {imageName};
      throw new ResourceNotFoundException(
          localeUtils.getMessageByKey(MessageKeys.ITEM_NOT_FOUND, obj));
    }
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(urlResource);
  }

  @PostMapping(value = "/images")
  @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
  public ResponseEntity<Map<String, Object>> uploadImage(
      @RequestPart(value = "upload") MultipartFile file,
      HttpServletRequest request
  ) throws IOException {

    String filename = storeFile(file, "");
    StringBuffer url = request.getRequestURL();
    url.append("/");
    url.append(filename);
    Map<String, Object> body = new HashMap<>();
    body.put(Constants.URL_STRING, url);
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

    /*@PostMapping("/generateFakerNewsData")
    public ResponseEntity<?> generateFakerNewsData() {
        Faker faker = new Faker();
        NewsEntity newsEntity;
        CategoryEntity category;
        for (int i = 0; i < 10E2; i++) {
            newsEntity = new NewsEntity();
            category = new CategoryEntity();
            category.setId((long)faker.number().numberBetween(1,8));
            newsEntity.setTitle(faker.commerce().productName());
            newsEntity.setShortDescription(faker.lorem().paragraph());
            newsEntity.setThumbnail("");
            newsEntity.setContent(faker.lorem().paragraph());
            newsEntity.setCategory(category);
            newsService.save(newsEntity);
        }
        return ResponseEntity.ok("Generate Faker News successfully");
    }*/
}
