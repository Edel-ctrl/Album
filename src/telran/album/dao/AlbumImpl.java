package telran.album.dao;

import telran.album.model.Photo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.function.Predicate;

public class AlbumImpl implements Album {
    private Photo[] photos;
    private int size;

    public AlbumImpl(int capacity) {
        photos = new Photo[capacity];
    }

    @Override
    public boolean addPhoto(Photo photo) {
        if (photo == null || photos.length == size || getPhotoFromAlbum(photo.getPhotoId(), photo.getAlbumId()) != null) { // если фото уже есть в альбоме
            return false;
        }
        photos[size++] = photo;
        return true;
    }

//    @Override
//    public boolean removePhoto(int photoId, int albumId) {
//        if (size == 0 || photos == null || photos.length == 0 || photoId < 0 || albumId < 0) {
//            return false;
//        }
//        for (int i = 0; i < size; i++) {
//            if (photoId == photos[i].getPhotoId() && albumId == photos[i].getAlbumId()) {
//                int numMoved = size - i - 1;
//                if (numMoved > 0) {
//                    System.arraycopy(photos, i + 1, photos, i, numMoved);
//                }
//                photos[--size] = null;
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    public boolean removePhoto(int photoId, int albumId) {
        if (size == 0 || photoId < 0 || albumId < 0) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (photoId == photos[i].getPhotoId() && albumId == photos[i].getAlbumId()) {
                int numMoved = size - i - 1;// количество элементов, которые нужно сдвинуть влево
                if (numMoved > 0) {
                    System.arraycopy(photos, i + 1, photos, i, numMoved);// копирую все элементы после удаленного на одну позицию влево (начиная с позиции i)
                }
                photos[--size] = null;
                return true;
            }
        }
        return false;
    }
//    @Override
//    public boolean removePhoto(int photoId, int albumId) {
//        if (size == 0 || photos == null || photos.length == 0 || photoId < 0 || albumId < 0) {
//            return false;
//
//        }
//        for (int i = 0; i < size; i++) {
//
//            if (photoId == photos[i].getPhotoId() && albumId == photos[i].getAlbumId()) {
//                // Сдвигаю все элементы после удаленного на одну позицию влево (начиная с позиции i)
//                for (int j = 0; j < size - 1; j++) {
//                    photos[j] = photos[j + 1];
//                }
//
//                photos[size - 1] = null;// Устанавливаю последний элемент в null (т.к. он теперь дублируется после сдвига)
//                size--;// Уменьшаю размер
//                return true;
//            }
//        }
//        return false;
//    }
@Override
public boolean updatePhoto(int photoId, int albumId, String url) {
    if (url == null) {
        return false;
    }
    for (int i = 0; i < size; i++) {
        if (photos[i].getPhotoId() == photoId && photos[i].getAlbumId() == albumId) {
            photos[i].setUrl(url);
            return true;
        }
    }
    return false;
}
//    @Override
//    public boolean updatePhoto(int photoId, int albumId, String url) {
//        String newUrl = url;
//        for (int i = 0; i < photos.length; i++) {
//            if (photos[i].getPhotoId() == photoId && photos[i].getAlbumId() == albumId && newUrl != null) {
//                photos[i].setUrl(newUrl);
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    public Photo getPhotoFromAlbum(int photoId, int albumId) {
        for (int i = 0; i < size; i++) {
            if (photos[i].getAlbumId() == albumId && photos[i].getPhotoId() == photoId) {
                return photos[i];
            }
        }
        return null;
    }
    @Override
    public Photo[] getAllPhotoFromAlbum(int albumId) {
        return findPhotosByPredicate(photo -> photo.getAlbumId() == albumId);
    }
//    @Override
//    public Photo[] getAllPhotoFromAlbum(int albumId) {
//// 72 и 73 строка эквивалент синтаксису (foreach) "for (Photo photo : photos) {"
//        int count = 0;
//        for (int i = 0; i < photos.length; i++) {
//            if (photos[i] != null && photos[i].getAlbumId() == albumId) { // проверяю, что я не пытаюсь получить albumId у null объекта( photos[i])
//                count++;
//            }
//        }
//        Photo[] photosFromAlbum = new Photo[count];
//        int index = 0;
//
//        for (int i = 0; i < photos.length; i++) {
//            if (photos[i] != null && photos[i].getAlbumId() == albumId) {
//                photosFromAlbum[index++] = photos[i];
//            }
//        }
//        return photosFromAlbum;
//    }
@Override
public Photo[] getPhotoBetweenDate(LocalDate dateFrom, LocalDate dateTo) {
    return findPhotosByPredicate(photo -> {// ищу фото по условию
        LocalDate photoDate = photo.getDate().toLocalDate();// преобразую дату фото в LocalDate
        return (photoDate.isAfter(dateFrom) || photoDate.isEqual(dateFrom)) &&
                photoDate.isBefore(dateTo);// если дата фото попадает в диапазон
    });
}
//    @Override
//    public Photo[] getPhotoBetweenDate(LocalDate dateFrom, LocalDate dateTo) {
//        int count = 0;
//
//        // Подсчитываю количество фото, которые попадают в диапазон дат
//        for (int i = 0; i < size; i++) {
////            if (photos[i] != null && photos[i].getDate() != null) {// проверяю, что я не пытаюсь получить date у null объекта( photos[i])
//
////            Преимущества использования size:
////            1. Уменьшает вероятность ошибок, так как не нужно помнить, что photos.length - это не количество элементов, а размер ма��сива.
////            2. Упрощает код, так как не нужно проверять, что photos[i] != null(если гарантируется, что все элементы до size не null.)
////            3. Улучшает читаемость кода, так как size - это количество элементов, а photos.length - размер массива.
////            4. Улучшает производительность, так как не нужно итерировать по неиспользуемым элементам ма��сива.
////
//
//            if (photos[i].getDate() != null) { //если вместо photos.length использую size, то проверка photos[i] != null не нужна
//                LocalDate photoDate = photos[i].getDate().toLocalDate();
//                // если дата фото попадает в диапазон,ВКЛЮЧАЯ ГРАНИЦЫ
//                if ((photoDate.isAfter(dateFrom) || photoDate.isEqual(dateFrom)) &&
//                        (photoDate.isBefore(dateTo) )) {
//                    count++;
//                }
//            }
//        }
//        // Создаю массив нужного размера
//        Photo[] photosBetweenDate = new Photo[count];
//        int index = 0;
//
//        // Заполняю массив фото, которые попадают в диапазон дат
//        for (int j = 0; j < size; j++) {
////                if (photos[j] != null && photos[j].getDate() != null) {// проверяю, что я не пытаюсь получить date у null объекта( photos[i])
//            if (photos[j].getDate() != null) {
//                LocalDate photoDate = photos[j].getDate().toLocalDate();
//
//                // если дата фото попадает в диапазон,ВКЛЮЧАЯ ГРАНИЦЫ
//            if ((photoDate.isAfter(dateFrom) || photoDate.isEqual(dateFrom)) &&
//                    (photoDate.isBefore(dateTo))) {
//                photosBetweenDate[index++] = photos[j];
//            }
//        }
//    }
//            return photosBetweenDate;
//}


    private Photo[] findPhotosByPredicate(Predicate<Photo> predicate) {// метод для поиска фото по условию
        Photo[] res = new Photo[size];
        int j = 0;// индекс для нового массива
        for (int i = 0; i < size; i++) {
            if (predicate.test(photos[i])) {// если фото удовлетворяет условию
                res[j++] = photos[i];// добавляю фото в новый массив
            }
        }
        return Arrays.copyOf(res, j);
    }
    @Override
    public int size() {
        return size;
    }
}