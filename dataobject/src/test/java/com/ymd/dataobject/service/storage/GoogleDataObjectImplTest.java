package com.ymd.dataobject.service.storage;

import com.ymd.dataobject.exception.ObjectNotFoundException;
import io.github.cdimascio.dotenv.Dotenv;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertThrows;

class GoogleDataObjectImplTest extends TestCase {

    private GoogleDataObjectImpl dataObject;
    private Dotenv dotenv;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        dotenv = Dotenv.load();
        this.dataObject = new GoogleDataObjectImpl();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Tests for doesExist method
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void doesExistReturnsTrueWhenObjectExists() {
        URI bucketUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI"));
        //given
        //The bucket is always available

        //when

        //then
        assertTrue(this.dataObject.doesExist(bucketUri));
    }

    @Test
    public void testDoesExist_ExistingObject_ObjectExists() throws Exception {
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");

        //given
        //The bucket is always available
        URI localUri = new File("dataobject/images/test.png").toURI();
        this.dataObject.upload(Files.readAllBytes(Paths.get(localUri)), objectUri);

        //when
        //check the assertion

        //then
        assertTrue(this.dataObject.doesExist(objectUri));
        this.dataObject.remove(objectUri, false);
    }

    @Test
    public void testDoesExist_MissingObject_ObjectNotExists() {
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");
        //given
        //The bucket is always available
        //The bucket is empty (or does not contain the expected object)

        //when
        //check the assertion

        //then
        assertFalse(this.dataObject.doesExist(objectUri));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Tests for upload method
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testUpload_BucketAndLocalFileAreAvailable_NewObjectCreatedOnBucket() throws Exception {
        URI bucketUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI"));
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.jpg");
        URI localFile = new File("dataobject/images/test.png").toURI();

        //given
        assertTrue(this.dataObject.doesExist(bucketUri));
        assertFalse(this.dataObject.doesExist(objectUri));

        //when
        this.dataObject.upload(Files.readAllBytes(Paths.get(localFile)), objectUri);

        //then
        assertTrue(this.dataObject.doesExist(objectUri));
        this.dataObject.remove(objectUri, false);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Tests for download method
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testDownload_ObjectAndLocalPathAvailable_ObjectDownloaded() throws Exception {
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");
        URI fileToUpload = new File("dataobject/images/test.png").toURI();
        URI destinationFile = new File("dataobject/images/downloaded.png").toURI();
        File file = Paths.get(destinationFile).toFile();

        //given
        this.dataObject.upload(Files.readAllBytes(Paths.get(fileToUpload)), objectUri);
        assertTrue(this.dataObject.doesExist(objectUri));
        assertFalse(file.exists());

        //when
        this.dataObject.download(destinationFile, objectUri);

        //then
        assertTrue(file.exists());

        file.delete();
        this.dataObject.remove(objectUri, false);
    }

    @Test
    public void testDownload_ObjectMissing_ThrowException() {
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");
        URI localFile = new File("dataobject/images/dontExist.png").toURI();
        File file = Paths.get(localFile).toFile();

        //given
        assertFalse(this.dataObject.doesExist(objectUri));
        assertFalse(file.exists());

        //when
        assertThrows(ObjectNotFoundException.class, () -> this.dataObject.download(localFile, objectUri));

        //then
        //Exception thrown
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Tests for publish method
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testPublish_ObjectExists_PublicUrlCreated() throws Exception {
        URI objectUriLocal = new File("dataobject/images/test.png").toURI();
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");
        this.dataObject.upload(Files.readAllBytes(Paths.get(objectUriLocal)), objectUri);

        URI destinationFolder = new File("dataobject/images/").toURI();
        File destinationFolderFile = Paths.get(destinationFolder).toFile();

        //given
        assertTrue(this.dataObject.doesExist(objectUri));
        assertTrue(destinationFolderFile.exists());

        //when
        URL presignedUrl = this.dataObject.publish(objectUri, 90);
        File downloadedFile = new File(destinationFolder.getPath() + "fileToTest.png");
        FileUtils.copyURLToFile(presignedUrl, downloadedFile);

        //then
        assertTrue(downloadedFile.exists());
        downloadedFile.delete();
        this.dataObject.remove(objectUri, false);
    }

    @Test
    public void testPublish_ObjectMissing_ThrowException() {

        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");

        //given
        assertFalse(this.dataObject.doesExist(objectUri));

        //when
        assertThrows(ObjectNotFoundException.class, () -> this.dataObject.publish(objectUri, 90));

        //then
        //Exception thrown
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Tests for remove method
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testRemove_ObjectPresentNoFolder_ObjectRemoved() throws Exception {

        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest.png");
        URI localFile = new File("dataobject/images/test.png").toURI();
        this.dataObject.upload(Files.readAllBytes(Paths.get(localFile)), objectUri);

        //given
        assertTrue(this.dataObject.doesExist(objectUri));

        //when
        this.dataObject.remove(objectUri, false);

        //then
        assertFalse(this.dataObject.doesExist(objectUri));
    }

    @Test
    public void testRemove_ObjectAndFolderPresent_ObjectRemoved() throws Exception {

        URI localFile = new File("dataobject/images/test.png").toURI();
        URI objectUri = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest");
        URI objectUriInSubFolder = URI.create(dotenv.get("GOOGLE_BUCKET_URI") + "fileToTest/test");

        this.dataObject.upload(Files.readAllBytes(Paths.get(localFile)), objectUri);
        this.dataObject.upload(Files.readAllBytes(Paths.get(localFile)), objectUriInSubFolder);

        //given
        //The bucket contains object at the root level as well as in subfolders
        //Sample: mybucket.com/myobject     //myObject is a folder
        //        mybucket.com/myobject/myObjectInSubfolder
        assertTrue(this.dataObject.doesExist(objectUri));
        assertTrue(this.dataObject.doesExist(objectUriInSubFolder));

        //when
        this.dataObject.remove(objectUri, true);

        //then
        assertFalse(this.dataObject.doesExist(objectUri));
        assertFalse(this.dataObject.doesExist(objectUriInSubFolder));
    }




}