package de.maaxgr.config.backend.dataaccess.config

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import java.io.File

class ConfigLoader {

    fun loadConfig(name: String = "config.yaml"): ConfigYaml {
        val configFileContent = File(name).readText()
        val yamlParser = createYamlParser()

        return yamlParser.decodeFromString(configFileContent)
    }

    private fun createYamlParser(): Yaml {
        val yamlConfig = Yaml.default.configuration.copy(
            polymorphismStyle = PolymorphismStyle.Property,
            polymorphismPropertyName = "type",
        )
        return Yaml(Yaml.default.serializersModule, yamlConfig)
    }

}
