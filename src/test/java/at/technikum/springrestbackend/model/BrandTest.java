package at.technikum.springrestbackend.model;
import at.technikum.springrestbackend.model.Brand;
import at.technikum.springrestbackend.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class BrandTest {

    @Test
    void testBrandConstructor() {
        // Arrange
        String name = "TestBrand";
        String picturePath = "/path/to/image.jpg";
        User mockUser = mock(User.class);

        // Act
        Brand brand = new Brand(name, picturePath, mockUser);

        // Assert
        assertEquals(name, brand.getName());
        assertEquals(picturePath, brand.getPicturePath());
        assertEquals(mockUser, brand.getCreatedBy());
    }

    // Additional test cases can be added to cover different scenarios or edge cases
}
