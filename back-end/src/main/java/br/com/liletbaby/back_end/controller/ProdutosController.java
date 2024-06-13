package br.com.liletbaby.back_end.controller;

import br.com.liletbaby.back_end.models.Produto;
import br.com.liletbaby.back_end.repository.ProdutoRepositorio;
import br.com.liletbaby.back_end.utils.NumericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ProdutosController class.
 *
 * @author Wender Couto
 * @author João Abreu
 * @since 0.0.1-SNAPSHOT
 */

@RestController
@RequestMapping("/store/api")
public class ProdutosController {

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @PostMapping("/cadastro")
    @ResponseBody
    public ResponseEntity<?> cadastroProdutos(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String sku = payload.get("sku");
        String color = payload.get("color");
        String size = payload.get("size");

        Integer stock = NumericConverter.safeParseInteger(payload.get("stock"));
        Double discount = NumericConverter.safeParseDouble(payload.get("discount"));
        Double price = NumericConverter.safeParseDouble(payload.get("price"));


        if (name == null || sku == null || color == null || size == null || name.isBlank() || sku.isBlank()
                || color.isBlank() || size.isBlank() || stock == null || discount == null || price == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos.");
        }

        Produto novoProduto = new Produto(name, price, stock, sku, discount, color, size);
        this.produtoRepositorio.save(novoProduto);
        return ResponseEntity.ok("Produto criado com sucesso.");
    }

}