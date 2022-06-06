use std::env;
use serde::{Serialize, Deserialize};

#[derive(Debug, Serialize, Deserialize)]
pub struct RepositoryDetail {
    id: i32,
    name: String,
    configs: Vec<RepositoryDetailConfig>
}

#[derive(Debug, Serialize, Deserialize)]
pub struct RepositoryDetailConfig {
    path: String,
    #[serde(rename = "accessibleVariants")]
    accessible_variants: Vec<RepositoryDetailConfigVariant>
}

#[derive(Debug, Serialize, Deserialize)]
pub struct RepositoryDetailConfigVariant {
    id: i32,
    name: String
}

pub fn get_repository_detail() -> RepositoryDetail  {

    let client = reqwest::blocking::Client::new();

    let resp2: Result<RepositoryDetail, reqwest::Error> = client.get("http://localhost:8077/repository/1")
        .header("Token", env::var("TOKEN").unwrap())
        .send()
        .unwrap()
        .json();

    println!("aaa {:?}", resp2);


    let x = RepositoryDetail {
        id: 1,
        name: String::from("xxx"),
        configs: Vec::new()
    };

    return x;
}
