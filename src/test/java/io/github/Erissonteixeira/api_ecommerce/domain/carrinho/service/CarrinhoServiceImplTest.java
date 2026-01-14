package io.github.Erissonteixeira.api_ecommerce.domain.carrinho.service;

import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.entity.CarrinhoEntity;
import io.github.Erissonteixeira.api_ecommerce.domain.carrinho.repository.CarrinhoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceImplTest {

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @InjectMocks
    private CarrinhoServiceImpl service;

    private CarrinhoEntity carrinho;

    @BeforeEach
    void setup() {
        carrinho = new CarrinhoEntity();
    }

    @Test
    void criarCarrinho_deveSalvarECriarUmCarrinho() {
        when(carrinhoRepository.save(any(CarrinhoEntity.class)))
                .thenReturn(carrinho);

        CarrinhoEntity resultado = service.criarCarrinho();

        assertNotNull(resultado);
        verify(carrinhoRepository, times(1)).save(any(CarrinhoEntity.class));
    }
}
