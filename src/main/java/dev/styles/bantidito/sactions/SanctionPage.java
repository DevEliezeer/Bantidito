package dev.styles.bantidito.sactions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class SanctionPage {

    private final int page;
    private final Set<Sanction> sanctions;
}
