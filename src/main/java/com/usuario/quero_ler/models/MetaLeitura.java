package com.usuario.quero_ler.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_metas_leitura")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetaLeitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer ano;

    private Integer metaLivrosAno;

    private Integer metaLivrosMes;

    private Integer metaPaginasDia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Builder.Default
    @OneToMany(mappedBy = "metaLeitura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LivroMeta> livrosMeta = new ArrayList<>();

    public boolean pertenceAoAnoAtual() {
        return ano.equals(LocalDate.now().getYear());
    }

    public boolean possuiAlgumaMeta() {
        return metaLivrosAno != null
                || metaLivrosMes != null
                || metaPaginasDia != null
                || !livrosMeta.isEmpty();
    }
}
