package kitchenpos.products.tobe.application;

import kitchenpos.products.tobe.domain.Product;
import kitchenpos.products.tobe.domain.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.products.tobe.Fixtures.friedChicken;
import static kitchenpos.products.tobe.Fixtures.seasonedChicken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final Product expected = friedChicken();
        when(productRepository.save(any(Product.class))).thenReturn(expected);
        // when
        final Product actual = productService.create(expected);

        // then
        assertProduct(expected, actual);
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "-1000")
    void create(final BigDecimal price) {
        // given
        final Product expected = friedChicken();

        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new Product(expected.getName(), price));
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final List<Product> productList = Arrays.asList(friedChicken(), seasonedChicken());
        given(productRepository.findAll()).willReturn(productList);

        // when
        final List<Product> actual = productService.list();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(productList);
    }

    private void assertProduct(final Product expected, final Product actual) {
        assertThat(actual).isNotNull();
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice())
        );
    }
}
