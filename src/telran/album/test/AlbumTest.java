package telran.album.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.album.dao.Album;
import telran.album.dao.AlbumImpl;
import telran.album.model.Photo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
///ggkgkgkgkgkgk
class AlbumTest {
    private final LocalDateTime now = LocalDateTime.now();
    private final int capacity = 6;
    private final Comparator<Photo> comparator = (p1, p2) -> {
        int res = Integer.compare(p1.getAlbumId(), p2.getAlbumId());
        return res != 0 ? res : Integer.compare(p1.getPhotoId(), p2.getPhotoId());
    }; // Сортировка по albumId, а затем по photoId
    private Album album;
    private Photo[] photos;

    @BeforeEach
    void setUp() {
        album = new AlbumImpl(capacity);
        photos = new Photo[capacity];
        photos[0] = new Photo(1, 1, "Title1", "url1", now.minusDays(7));
        photos[1] = new Photo(1, 2, "Title2", "url2", now.minusDays(6));
        photos[2] = new Photo(1, 3, "Title3", "url3", now.minusDays(5));
        photos[3] = new Photo(2, 1, "Title1", "url1", now.minusDays(4));
        photos[4] = new Photo(2, 4, "Title4", "url4", now.minusDays(3));
        photos[5] = new Photo(1, 4, "Title4", "url4", now.minusDays(2));
        for (int i = 0; i < photos.length - 1; i++) {
            album.addPhoto(photos[i]);
        }
    }

    @Test
    void testAddPhoto() {
        assertFalse(album.addPhoto(null));
        assertFalse(album.addPhoto(photos[2]));
        assertTrue(album.addPhoto(photos[5]));
        assertEquals(capacity, album.size());
        assertFalse(album.addPhoto(new Photo(10, 4, "Title4", "url4", now.minusDays(2))));
    }


    @Test
    void testRemovePhoto() {
        assertFalse(album.removePhoto(11, 11)); // Фото с photoId=11 и albumId=11 не существует
        assertTrue(album.removePhoto(1, 1));  // Успешно удалла фотографию с photoId=1 и albumId=1
        assertEquals(4, album.size());        // Проверяю, что размер альбома уменьшился на 1
    }


    @Test
    void testUpdatePhoto() {
        assertTrue(album.updatePhoto(2, 1, "newUrl"));
        assertEquals("newUrl", album.getPhotoFromAlbum(2, 1).getUrl());

    }

    @Test
    void testGetPhotoFromAlbum() {
        assertEquals(photos[0], album.getPhotoFromAlbum(1, 1));
        assertNull(album.getPhotoFromAlbum(4, 1));
    }

    @Test
    void testGetAllPhotoFromAlbum() {
        Photo[] actual = album.getAllPhotoFromAlbum(2); // { photos[4], photos[3] }
        Arrays.sort(actual, comparator);// Сортирую массив actual
        Photo[] expected = {photos[3], photos[4]};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testGetPhotoBetweenDate() {
        LocalDate localDate = LocalDate.now();
        Photo[] actual = album.getPhotoBetweenDate(localDate.minusDays(6), localDate.minusDays(3));
        Arrays.sort(actual, comparator);
        Photo[] expected = {photos[1], photos[2], photos[3]};
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSize() {
        assertEquals(capacity - 1, album.size());
    }
}