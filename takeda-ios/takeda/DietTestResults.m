//
//  DietTestResults.m
//  takeda
//
//  Created by Alexander Rudenko on 05.03.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "DietTestResults.h"
#import "DietTest.h"
#import "ResultRiskAnal.h"

@interface DietTestResults ()

@end

@implementation DietTestResults
@synthesize result;
@synthesize resultArray;
@synthesize titleText;
@synthesize messageText;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    resultArray = [NSMutableArray new];
    [resultArray addObject:[self itemForType:@"red"]];
    [resultArray addObject:[self itemForType:@"blue"]];
    [self showInfo];
}

-(void)setupInterface{
    self.tableView.sectionHeaderHeight = 0.0;
    self.tableView.sectionFooterHeight = 0.0;
    self.tableView.separatorColor = [UIColor colorWithWhite:1.0f alpha:0.4f];
    self.messageText.font = [UIFont fontWithName:@"Helvetica-Light" size:12.0];
    self.titleText.font = [UIFont fontWithName:@"SegoeUI-Light" size:17.0];
    
    UIView *topview = [[UIView alloc] initWithFrame:CGRectMake(0,-self.tableView.height,self.tableView.width,self.tableView.height)];
    topview.backgroundColor = [UIColor whiteColor];
    [self.tableView addSubview:topview];
    
    self.makeTestRep.layer.borderColor = RGB(53, 65, 71).CGColor;
    [self.makeTestRep setTitleColor:RGB(53, 65, 71) forState:UIControlStateNormal];
    self.makeTestRep.layer.borderWidth = 1.0f;
    self.makeTestRep.titleLabel.font = [UIFont fontWithName:@"SegoeUI-Light" size:14];
    self.makeTestRep.clipsToBounds = YES;
    self.makeTestRep.layer.cornerRadius = 5.0f;


}

-(NSMutableDictionary*)itemForType:(NSString*)key{
    NSMutableDictionary *item = [NSMutableDictionary new];
    [item setObject:result[key] forKey:@"items"];
    [item setObject:key forKey:@"type"];
    return item;
}

-(void)showInfo{
    
    if ([result[@"blue"] isKindOfClass:[NSArray class]]&&[result[@"blue"] count]>0){
        self.view.backgroundColor = [self BlueColor];
    } else if ([result[@"red"] isKindOfClass:[NSArray class]]&&[result[@"red"] count]>0){
        self.view.backgroundColor = [self RedColor];
    } else {
        self.view.backgroundColor = RGB(243, 243, 243);
    }
   
    NSArray *messages = result[@"messages"];
    NSMutableString *messageStr = [NSMutableString new];
    for (int i = 0; i<messages.count; i++){
         id msg = messages[i];
        if ([Global isNotNull:msg]&&[msg isKindOfClass:[NSString class]]&&[(NSString*)msg length]>0){
            [messageStr appendFormat:@"%@",msg];
        }
        if (i!=messages.count-1){
          //  [messageStr appendString:@"\n\n"];
        }
    }

    self.messageText.text = messageStr;
    self.titleText.text = @"";

    self.titleText.height = [Global measureHeightOfUITextView:titleText];
    self.messageText.height = [Global measureHeightOfUITextView:messageText];

    self.titleText.editable = NO;
    self.messageText.editable = NO;

    self.messageText.y = self.titleText.bottom;
    [self.headerView setupAutosizeBySubviewsWithBottomDistance:10];
    self.tableView.tableHeaderView = self.headerView;
    [self.tableView reloadData];
}

#pragma mark - UITableView delegate&DataSource

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    if ([resultArray[section][@"items"] count]==0) return nil;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.tableView.frame.size.width,  54)];
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(15, 0, self.tableView.frame.size.width-15, 54)];
    label.textColor = [UIColor whiteColor];
    [label setFont:[UIFont fontWithName:@"SegoeWP-Light" size:17.0]];
    label.text = [self settingsForType:resultArray[section]][@"title"];
    [view addSubview:label];
    view.backgroundColor = [self settingsForType:resultArray[section]][@"background"];
//    UIImageView *topSepar = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, view.width, 0.5)];
//    topSepar.backgroundColor = RGB(152, 198, 245);
//    
//    UIImageView *bottomSepar = [[UIImageView alloc] initWithFrame:CGRectMake(0, 54-0.5f, view.width, 0.5)];
//    bottomSepar.backgroundColor = RGB(152, 198, 245);
//    
//    [view addSubview:topSepar];
//    [view addSubview:bottomSepar];

    return view;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return [resultArray[section][@"items"] count]>0?54:0;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    SWITCH(resultArray[indexPath.section][@"type"]){
        CASE(@"red"){
            riskCell *cell = (riskCell*)[self tableView:tableView cellForRowAtIndexPath:indexPath];
            float titleH = [Global heightLabel:cell.text_name];
            float textH = [Global heightLabel:cell.text_data];
            float cellH = titleH + textH + 23;
            return cellH;
        }
        CASE(@"blue"){
            StandartCombyCell *cell = (StandartCombyCell*)[self tableView:tableView cellForRowAtIndexPath:indexPath];
            float textH = [Global heightLabel:cell.caption];
            float cellH = textH + 20;
            return MAX(55, cellH);
        }
    }
    
    return 0;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return resultArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [resultArray[section][@"items"] count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    SWITCH(resultArray[indexPath.section][@"type"]){
        CASE(@"red"){
            return [self tableView:tableView cellForRowAtIndexPathRed:indexPath];
        }
        CASE(@"blue"){
            return [self tableView:tableView cellForRowAtIndexPathBlue:indexPath];
        }
    }

    
    return [UITableViewCell new];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPathRed:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"riskCell";
    riskCell *cell = nil;
    cell = (riskCell *) [self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"riskCell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[riskCell class]])
            {
                cell = (riskCell *)currentObject;
                break;
            }
        }
    }
    
    NSMutableDictionary *item = resultArray[indexPath.section][@"items"][indexPath.row];
    cell.img_icon.image = [UIImage imageNamed:[NSString stringWithFormat:@"attention_small"]];
    
    NSString *titleTxt;
    NSString *textTxt;
    
    if (item[@"title"]&&[item[@"title"] isKindOfClass:[NSString class]]){
        titleTxt = item[@"title"];
    } else {
        titleTxt = @"";
    }

    if (item[@"note"]&&[item[@"note"] isKindOfClass:[NSString class]]){
        textTxt = item[@"note"];
    } else {
        textTxt = @"";
    }
    cell.arrowRight.hidden = YES;
    
    cell.text_name.text = titleTxt;
    cell.text_data.text = textTxt;

    cell.text_name.font = [self cellTitleFont];
    cell.text_data.font = [self cellTextFont];
    
    cell.text_name.height = [Global heightLabel:cell.text_name];
    cell.text_data.height = [Global heightLabel:cell.text_data];
    cell.text_data.y = cell.text_name.bottom +4;
    
    cell.backgroundColor = [self settingsForType:resultArray[indexPath.section]][@"background"];
    
//    UIView *sel_view = [[UIView alloc] init];
//    sel_view.backgroundColor = [UIColor clearColor];
//    cell.selectedBackgroundView = sel_view;
    
    return cell;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPathBlue:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"StandartCombyCell";
    
    StandartCombyCell *cell = (StandartCombyCell *)[self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"StandartCombyCell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[StandartCombyCell class]])
            {
                cell = (StandartCombyCell *)currentObject;
                break;
            }
        }
    }
    
    NSMutableDictionary *item = resultArray[indexPath.section][@"items"][indexPath.row];
    
    cell.cellType = ctSimpleLeftCaption;
    cell.caption.text = item[@"text"];
    cell.caption.numberOfLines = 0;
    cell.caption.font = [self cellTextFont];
    cell.caption.textColor = [UIColor whiteColor];
    cell.backgroundColor = [self settingsForType:resultArray[indexPath.section]][@"background"];
    
    return cell;
}

#pragma mark -

-(IBAction)makeTest:(id)sender{

    [(ResultRiskAnal*)self.parentVC goToTest];
    
}

-(void)backAction{
    [self.navigationController popViewControllerAnimated:YES];

//    NSMutableArray *allViewControllers = [NSMutableArray arrayWithArray: self.navigationController.viewControllers];
//    [allViewControllers removeObjectIdenticalTo: self.parentVC];
//    self.navigationController.viewControllers = allViewControllers;
//    [self.navigationController popViewControllerAnimated:YES];

}


-(NSDictionary*)settingsForType:(NSMutableDictionary*)typeItem{
    NSDictionary *m = @{@"red":@{@"title":@"Следует предпочесть",@"background": [self RedColor]},@"blue":@{@"title":@"Следует употреблять умеренно",@"background":[self BlueColor]}};
    return m[typeItem[@"type"]];
}

-(UIFont*)fontDescription{
    return [UIFont fontWithName:@"SegoeUI-Light" size:15.0];
}

-(UIFont*)cellTitleFont{
    return [UIFont fontWithName:@"SegoeUI-Light" size:14.0];
}

-(UIFont*)cellTextFont{
    return [UIFont fontWithName:@"SegoeUI-Light" size:12.0];
}

-(UIColor*)BlueColor{
    return RGB(108, 174, 241);
}

-(UIColor*)RedColor{
    return RGB(240, 66, 69);
}

@end
