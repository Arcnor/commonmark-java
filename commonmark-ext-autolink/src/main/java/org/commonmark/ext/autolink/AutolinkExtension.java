package org.commonmark.ext.autolink;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.internal.AutolinkPostProcessor;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.nibor.autolink.internal.EmailScanner;
import org.nibor.autolink.internal.Scanner;
import org.nibor.autolink.internal.UrlScanner;

/**
 * Extension for automatically turning plain URLs and email addresses into links.
 * <p>
 * Create it with {@link #create()} and then configure it on the builders
 * ({@link org.commonmark.parser.Parser.Builder#extensions(Iterable)},
 * {@link HtmlRenderer.Builder#extensions(Iterable)}).
 * </p>
 * <p>
 * The parsed links are turned into normal {@link org.commonmark.node.Link} nodes.
 * </p>
 */
public class AutolinkExtension implements Parser.ParserExtension {

    private final AutolinkPostProcessor.Builder processorBuilder;

    private AutolinkExtension(AutolinkPostProcessor.Builder processorBuilder) {
        this.processorBuilder = processorBuilder;
    }

    public static Extension create() {
        return builder()
                .withScanner(UrlScanner.TRIGGER, new UrlScanner())
                .withScanner(EmailScanner.TRIGGER, new EmailScanner(true))
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.postProcessor(processorBuilder.build());
    }

    public static class Builder {

        private AutolinkPostProcessor.Builder processorBuilder = AutolinkPostProcessor.builder();

        private Builder() {
        }


        public Builder withScanner(char trigger, Scanner scanner) {
            processorBuilder.withScanner(trigger, scanner);

            return this;
        }

        public AutolinkExtension build() {
            return new AutolinkExtension(processorBuilder);
        }
    }
}
